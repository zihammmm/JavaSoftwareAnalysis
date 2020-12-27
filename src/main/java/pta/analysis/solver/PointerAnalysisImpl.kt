package pta.analysis.solver

import bamboo.util.AnalysisException
import callgraph.CallGraph
import callgraph.CallKind
import callgraph.Edge
import pta.analysis.ProgramManager
import pta.analysis.context.ContextInsensitiveSelector
import pta.analysis.context.DefaultContext
import pta.analysis.context.IContext
import pta.analysis.data.*
import pta.analysis.heap.HeapModel
import pta.analysis.selector.CallSiteSelector
import pta.analysis.selector.ContextSelector
import pta.element.*
import pta.set.PointsToSet
import pta.set.PointsToSetFactory
import pta.statement.Allocation
import pta.statement.Assign
import pta.statement.Call

class PointerAnalysisImpl constructor(
    override var heapModel: HeapModel,
    override var programManager: ProgramManager,
    override var pointsToSetFactory: PointsToSetFactory,
    override var dataManager: DataManager,
    override var contextSelector: ContextSelector = ContextInsensitiveSelector()
) : PointerAnalysis {

    private lateinit var onFlyCallGraph: OnFlyCallGraph

    private lateinit var workList: WorkList

    private lateinit var pointerFlowGraph: PointerFlowGraph

    override var contextSensitive: Boolean = contextSelector !is ContextInsensitiveSelector

    override val callGraph: CallGraph<CSCallSite, CSMethod>
        get() = onFlyCallGraph

    override val variables: Sequence<CSVariable>
        get() = dataManager.getCSVariables()

    override val instanceFields: Sequence<InstanceField>
        get() = dataManager.getInstanceFields()

    override fun initialize() {
        onFlyCallGraph = OnFlyCallGraph(dataManager)
        pointerFlowGraph = PointerFlowGraph()
        workList = WorkList()
        for (entry in programManager.getEntryMethods()) {
            val csMethod = dataManager.getCSMethod(contextSelector.getDefaultContext(), entry)
            processNewMethod(csMethod)
            onFlyCallGraph.addEntryMethod(csMethod)
        }
    }

    override fun solve() {
        initialize()
        analyze()
    }

    private fun analyze() {
        while (!workList.isEmpty) {
            while (workList.hasPointerEntries) {
                val entry = workList.pollPointerEntry()
                val p = entry.pointer
                val pts = entry.pointsToSet
                val diff = propagate(p, pts)
                if (p is CSVariable) {
                    val context = p.context
                    val v = p.variable
                    processInstanceStore(context, v, diff)
                    processInstanceLoad(context, v, p, diff)
                    processCall(context, v, p, diff)
                }
            }
            while (workList.hasCallEdges) {
                processCallEdge(workList.pollCallEdge())
            }
        }
    }

    fun propagate(pointer: Pointer, pointsToSet: PointsToSet): PointsToSet {
        val diff = pointsToSetFactory.makePointsToSet()
        pointsToSet.sequence().forEach { obj ->
            if (pointer.pointsToSet!!.addObject(obj)) {
                diff.addObject(obj)
            }
        }

        if (!diff.isEmpty()) {
            pointerFlowGraph.getOutEdgesOf(pointer).forEach { edge ->
                workList.addPointerEntry(edge.to, diff)
            }
        }

        return diff
    }

    private fun processNewMethod(csMethod: CSMethod) {
        if (onFlyCallGraph.addNewMethod(csMethod)) {
            val context = csMethod.context
            val method = csMethod.method
            for (stmt in method.getStatements()) {
                when (stmt) {
                    is Allocation -> processAllocation(csMethod, context, stmt)
                    is Assign -> processLocalAssign(context, stmt)
                    is Call -> processStaticCalls(context, stmt.callSite)
                }
            }
        }
    }

    private fun processAllocation(csMethod: CSMethod, context: IContext, alloc: Allocation) {
        val obj = heapModel.getObj(alloc)
        val heapContext = contextSelector.selectHeapContext(csMethod, obj)
        val csObj = dataManager.getCSObj(heapContext, obj)
        val lhs = dataManager.getCSVariable(context, alloc.variable)
        workList.addPointerEntry(lhs, pointsToSetFactory.makePointsToSet(csObj))
    }

    private fun processLocalAssign(context: IContext, assign: Assign) {
        val from = dataManager.getCSVariable(context, assign.from)
        val to = dataManager.getCSVariable(context, assign.to)
        addPFGEdge(from, to, PointerFlowEdge.Kind.LOCAL_ASSIGN)
    }

    private fun processStaticCalls(context: IContext, callSite: CallSite) {
        if (callSite.isStatic) {
            val callee = callSite.method!!
            val csCallSite = dataManager.getCSCallSite(context, callSite)
            val calleeCtx = contextSelector.selectContext(csCallSite, callee)
            val csCallee = dataManager.getCSMethod(calleeCtx, callee)
            val edge = Edge(CallKind.STATIC, csCallSite, csCallee)
            workList.addCallEdge(edge)
        }
    }

    private fun processCall(context: IContext, variable: Variable, recv: CSVariable, pts: PointsToSet) {
        for (call in variable.getCalls()) {
            val callSite = call.callSite
            for (recvObj in pts) {
                val callee = resolveCallee(recvObj.obj, callSite)
                val csCallSite = dataManager.getCSCallSite(context, callSite)
                val calleeContext = contextSelector.selectContext(csCallSite, recvObj, callee)
                val callKind = callSite.callKind()
                val csCallee = dataManager.getCSMethod(calleeContext, callee)
                workList.addCallEdge(Edge(callKind, csCallSite, csCallee))
                val thisVar = dataManager.getCSVariable(calleeContext, callee.getThis())
                workList.addPointerEntry(thisVar, pointsToSetFactory.makePointsToSet(recvObj))
            }
        }
    }

    private fun addPFGEdge(from: Pointer, to: Pointer, kind: PointerFlowEdge.Kind) {
        if (pointerFlowGraph.addEdge(from, to, kind) && !from.pointsToSet!!.isEmpty()) {
            workList.addPointerEntry(to, from.pointsToSet!!)
        }
    }

    private fun resolveCallee(recvObj: Obj, callSite: CallSite): Method {
        return when {
            callSite.isInterface || callSite.isVirtual ->
                programManager.resolveInterfaceOrVirtualCall(recvObj.getType(), callSite.method!!)
            callSite.isSpecial ->
                programManager.resolveSpecialCall(callSite, callSite.containerMethod!!)
            else ->
                throw AnalysisException("Unknown CallSite: $callSite")
        }
    }

    private fun processCallEdge(edge: Edge<CSCallSite, CSMethod>) {
        if (!onFlyCallGraph.containsEdge(edge)) {
            onFlyCallGraph.addEdge(edge)
            val csCallee = edge.callee
            processNewMethod(csCallee)
            val callerCtx = edge.callSite.context
            val calleeCtx = csCallee.context
            val callSite = edge.callSite.callSite
            val callee = csCallee.method
            val args = callSite.arguments
            val params = callee.getParameters()
            for (i in params.indices) {
                val arg = dataManager.getCSVariable(calleeCtx, args[i])
                val param = dataManager.getCSVariable(calleeCtx, params[i])
                addPFGEdge(arg, param, PointerFlowEdge.Kind.PARAMETER_PASSING)
            }
            callSite.call!!.lhs?.let {
                val lhs = dataManager.getCSVariable(callerCtx, it)
                for (ret in callee.getReturnVariables()) {
                    val retVar = dataManager.getCSVariable(calleeCtx, ret)
                    addPFGEdge(retVar, lhs, PointerFlowEdge.Kind.RETURN)
                }
            }
        }
    }

    private fun processInstanceLoad(context: IContext, variable: Variable, base: CSVariable, pts: PointsToSet) {
        variable.getLoads().forEach { load ->
            val to = dataManager.getCSVariable(context, load.getTo())
            pts.forEach { baseObj ->
                val instanceField = dataManager.getInstanceField(baseObj, load.getField())
                addPFGEdge(instanceField, to, PointerFlowEdge.Kind.INSTANCE_LOAD)
            }
        }
    }

    private fun processInstanceStore(context: IContext, variable: Variable, pts: PointsToSet) {
        variable.getStores().forEach { store ->
            val from = dataManager.getCSVariable(context, store.getFrom())
            pts.forEach { baseObj ->
                val instanceField = dataManager.getInstanceField(baseObj, store.getField())
                addPFGEdge(from, instanceField, PointerFlowEdge.Kind.INSTANCE_STORE)
            }
        }
    }
}