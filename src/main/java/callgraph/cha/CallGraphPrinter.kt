package callgraph.cha

import logger.Logger
import soot.Body
import soot.BodyTransformer
import soot.BriefUnitPrinter
import soot.SootMethod
import util.resultchecker.CHAResultChecker
import util.unitToString

object CallGraphPrinter: BodyTransformer() {
    const val TAG = "CallGraphPrinter"

    var isOutput = true

    @Synchronized
    override fun internalTransform(p0: Body?, p1: String?, p2: MutableMap<String, String>?) {
        if (p0 != null) {
            val callGraph = CHACallGraphBuilder.recentCallGraph
            if (isOutput) {
                var hasCallees = false
                val up = BriefUnitPrinter(p0)
                for (unit in p0.units) {
                    val callees = callGraph.getCallees(unit)
                    if (callees.isNotEmpty()) {
                        if (!hasCallees) {
                            hasCallees = true
                            Logger.i(TAG, "------ ${p0.method} [call graph] ------")
                        }
                        Logger.i(TAG, "${unitToString(up, unit)} -> ${calleesToString(callees)}")
                    }
                }
                if (hasCallees) {
                    println()
                }
            }
            CHAResultChecker.compare(p0, callGraph)
        }
    }

    fun calleesToString(callees: Collection<SootMethod>): String {
        callees.sortedWith(compareBy{
            it.toString()
        })
        return callees.toString()
    }
}