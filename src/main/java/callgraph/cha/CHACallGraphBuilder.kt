package callgraph.cha

import bamboo.util.AnalysisException
import callgraph.CallGraph
import callgraph.CallKind
import callgraph.JimpleCallGraph
import callgraph.callKind
import soot.*
import soot.Unit
import soot.jimple.Stmt
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.HashSet

object CHACallGraphBuilder : SceneTransformer() {

    private lateinit var hierarchy: FastHierarchy
    lateinit var recentCallGraph: CallGraph<Unit, SootMethod>

    override fun internalTransform(p0: String?, p1: MutableMap<String, String>?) {
        hierarchy = Scene.v().orMakeFastHierarchy
        val callGraph = build()
        recentCallGraph = callGraph
    }

    fun build(): CallGraph<Unit, SootMethod> {
        val callGraph = JimpleCallGraph()
        callGraph.addEntryMethod(Scene.v().mainMethod)
        buildCallGraph(callGraph)
        return callGraph
    }

    private fun buildCallGraph(callGraph: JimpleCallGraph) {
        val workList = ArrayDeque(callGraph.getEntryMethods())
        while (workList.isNotEmpty()) {
            val m = workList.removeFirst()
            if (callGraph.contains(m)) {
                for (callSite in callGraph.getCallSitesIn(m)) {
                    val target = resolveCalleesOf(callSite)
                    val kind = callSite.callKind
                    for (targetMethod in target) {
                        callGraph.addEdge(callSite, targetMethod, kind)
                        workList.add(targetMethod)
                    }
                }
            }
        }
    }

    private fun resolveCalleesOf(callSite: Unit): Set<SootMethod> {
        val invoke = (callSite as Stmt).invokeExpr
        val method = invoke.method
        return when (callSite.callKind) {
            CallKind.INTERFACE, CallKind.VIRTUAL -> {
                val targets = HashSet<SootMethod>()
                val c = invoke.methodRef.declaringClass
                val classes: ArrayDeque<SootClass>
                if (c.isInterface) {
                    classes = ArrayDeque(hierarchy.getAllImplementersOfInterface(c))
                } else {
                    classes = ArrayDeque()
                    classes.add(c)
                }
                while (classes.isNotEmpty()) {
                    val parent = classes.removeFirst()
                    dispatch(parent, method)?.let {
                        targets.add(it)
                    }
                    for (childClass in hierarchy.getSubclassesOf(parent)) {
                        classes.add(childClass)
                    }
                }
                targets
            }
            CallKind.SPECIAL, CallKind.STATIC -> Collections.singleton(method)
            else -> throw AnalysisException("unknown invocation expression: $invoke")
        }
    }

    private fun dispatch(cls: SootClass?, method: SootMethod): SootMethod? {
        if (cls == null) {
            return null
        }
        val m = cls.getMethodUnsafe(method.subSignature)
        return if (m == null || !m.isConcrete) {
            dispatch(cls.superclassUnsafe, method)
        } else {
            m
        }
    }
}