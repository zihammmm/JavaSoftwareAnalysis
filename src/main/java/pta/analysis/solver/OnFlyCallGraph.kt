package pta.analysis.solver

import pta.analysis.data.CSCallSite
import callgraph.AbstractCallGraph
import callgraph.Edge
import pta.analysis.data.CSMethod
import pta.analysis.data.DataManager
import pta.statement.Call
import util.addToMapSet

class OnFlyCallGraph constructor(
    private val dataManager: DataManager
) : AbstractCallGraph<CSCallSite, CSMethod>() {

    fun addEdge(edge: Edge<CSCallSite, CSMethod>) =
        callSiteToEdges.addToMapSet(edge.callSite, edge) || calleeToEdges.addToMapSet(edge.callee, edge)

    fun containsEdge(edge: Edge<CSCallSite, CSMethod>) = getEdgesOf(edge.callSite).contains(edge)

    override fun addNewMethod(csMethod: CSMethod): Boolean {
        return if (reachableMethods.add(csMethod)) {
            val method = csMethod.method
            val context = csMethod.context

            for (stmt in method.getStatements()) {
                if (stmt is Call) {
                    val callSite = stmt.callSite
                    val csCallSite = dataManager.getCSCallSite(context, callSite)
                    callSiteToContainer[csCallSite] = csMethod
                    callSiteIn.addToMapSet(csMethod, csCallSite)
                }
            }
            true
        } else {
            false
        }
    }
}