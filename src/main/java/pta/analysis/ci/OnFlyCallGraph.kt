package pta.analysis.ci

import callgraph.AbstractCallGraph
import callgraph.Edge
import pta.element.CallSite
import pta.element.Method
import pta.statement.Call
import util.addToMapSet

class OnFlyCallGraph : AbstractCallGraph<CallSite, Method>() {

    fun addEdge(edge: Edge<CallSite, Method>) =
        callSiteToEdges.addToMapSet(edge.callSite, edge) || calleeToEdges.addToMapSet(edge.callee, edge)

    fun containsEdge(edge: Edge<CallSite, Method>) = getEdgesOf(edge.callSite).contains(edge)

    override fun addNewMethod(method: Method): Boolean {
        return if (reachableMethods.add(method)) {
            for (stmt in method.getStatements()) {
                if (stmt is Call) {
                    val callSite = stmt.callSite
                    callSiteToContainer[callSite] = method
                    callSiteIn.addToMapSet(method, callSite)
                }
            }
            true
        } else {
            false
        }
    }
}