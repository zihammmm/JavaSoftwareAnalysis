package pta.analysis.solver

import bamboo.util.AnalysisException
import base.IAnalysis
import callgraph.CallKind
import callgraph.Edge
import pta.analysis.ProgramManager
import pta.analysis.heap.HeapModel
import pta.element.CallSite
import pta.element.Method
import pta.element.Obj
import pta.element.callKind
import pta.statement.Allocation
import pta.statement.Assign
import pta.statement.Call

class PointerAnalysis constructor(
    private val heapModel: HeapModel,
    private val programManager: ProgramManager
) : IAnalysis {

    private lateinit var callGraph: OnFlyCallGraph

    private lateinit var workList: WorkList

    private lateinit var pointerFlowGraph: PointerFlowGraph

    fun getVariables(): Sequence<Var> {
        return pointerFlowGraph.getPointer().asSequence()
            .filter { it is Var }
            .map { it as Var }
    }

    fun getInstanceFields(): Sequence<InstanceField> {
        return pointerFlowGraph.getPointer().asSequence()
            .filter { it is InstanceField }
            .map { it as InstanceField }
    }

    override fun initialize() {
        callGraph = OnFlyCallGraph()
        pointerFlowGraph = PointerFlowGraph()
        workList = WorkList()
        for (entry in programManager.getEntryMethods()) {
            addReachable(entry)
            callGraph.addEntryMethod(entry)
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
                if (p is Var) {
                    processInstanceStore(p, diff)
                    processInstanceLoad(p, diff)
                    processCall(p, diff)
                }
            }
            while (workList.hasCallEdges) {
                processCallEdge(workList.pollCallEdge())
            }
        }
    }

    fun propagate(pointer: AbstractPointer, pointsToSet: PointsToSet): PointsToSet {
        val diff = PointsToSet()
        pointsToSet.sequence().forEach { obj->
            if (pointer.getPointsToSet().addObject(obj)) {
                diff.addObject(obj)
            }
        }

        if (!diff.isEmpty) {
            pointerFlowGraph.getSuccessorsOf(pointer).asSequence().forEach { s ->
                workList.addPointerEntry(s, diff)
            }
        }

        return diff
    }

    private fun addReachable(method: Method) {
        if (callGraph.addNewMethod(method)) {
            for (statement in method.getStatements()) {
                when (statement) {
                    is Allocation -> processAllocation(statement, method)
                    is Assign -> processLocalAssign(statement)
                    is Call -> processStaticCalls(statement)
                }
            }
        }
    }

    private fun processAllocation(statement: Allocation, method: Method) {
        val variable = statement.variable
        val allocationSite = statement.allocationSite
        val obj = heapModel.getObj(allocationSite, statement.type, method)
        workList.addPointerEntry(pointerFlowGraph.getVar(variable), PointsToSet(obj))
    }

    private fun processLocalAssign(statement: Assign) {
        val to = pointerFlowGraph.getVar(statement.to)
        val from = pointerFlowGraph.getVar(statement.from)
        addPFGEdge(from, to)
    }

    private fun processStaticCalls(statement: Call) {
        val callSite = statement.callSite
        if (callSite.isStatic) {
            val callee = callSite.method!!
            workList.addCallEdge(Edge(CallKind.STATIC, callSite, callee))
        }
    }

    private fun processCall(recv: Var, pts: PointsToSet) {
        val variable = recv.getVariable()
        for(call in variable.getCalls()) {
            val callSite = call.callSite
            for (recvObj in pts) {
                val callee = resolveCallee(recvObj, callSite)
                val thisVar = pointerFlowGraph.getVar(callee.getThis())
                workList.addPointerEntry(thisVar, PointsToSet(recvObj))
                workList.addCallEdge(Edge(callSite.callKind(), callSite, callee))
            }
        }
    }

    private fun addPFGEdge(from: AbstractPointer, to: AbstractPointer) {
        if (pointerFlowGraph.addEdge(from, to)) {
            if (!from.getPointsToSet().isEmpty) {
                workList.addPointerEntry(to, from.getPointsToSet())
            }
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

    private fun processCallEdge(edge: Edge<CallSite, Method>) {
        if (!callGraph.containsEdge(edge)) {
            callGraph.addEdge(edge)
            val callee = edge.callee
            addReachable(callee)
            val callSite = edge.callSite
            val args = callSite.arguments
            val params = callee.getParameters()
            for (i in params.indices) {
                val arg = pointerFlowGraph.getVar(args[i])
                val param = pointerFlowGraph.getVar(params[i])
                addPFGEdge(arg, param)
            }
            callSite.call!!.lhs?.let {
                val lhs = pointerFlowGraph.getVar(it)
                for (ret in callee.getReturnVariables()) {
                    val retVar = pointerFlowGraph.getVar(ret)
                    addPFGEdge(retVar, lhs)
                }
            }
        }
    }

    private fun processInstanceLoad(base: Var, pts: PointsToSet) {
        base.getVariable().getStores()
            .forEach { obj ->
                pts.asSequence().forEach { pt ->
                    addPFGEdge(
                        pointerFlowGraph.getVar(obj.getFrom()),
                        pointerFlowGraph.getInstanceField(pt, obj.getField())
                    )
                }
            }
    }

    private fun processInstanceStore(base: Var, pts: PointsToSet) {
        base.getVariable().getLoads().forEach { obj ->
            pts.asSequence().forEach { pt ->
                addPFGEdge(
                    pointerFlowGraph.getInstanceField(pt, obj.getField()),
                    pointerFlowGraph.getVar(obj.getTo())
                )
            }
        }
    }
}