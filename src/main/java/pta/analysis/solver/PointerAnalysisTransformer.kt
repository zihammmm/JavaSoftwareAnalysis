package pta.analysis.solver

import logger.Logger
import pta.analysis.context.ContextInsensitiveSelector
import pta.analysis.data.CSVariable
import pta.analysis.data.InstanceField
import pta.analysis.data.MapBasedDataManager
import pta.analysis.data.Pointer
import pta.analysis.heap.AllocationSiteBasedModel
import pta.analysis.selector.*
import pta.jimple.JimplePointerAnalysis
import pta.jimple.JimpleProgramManager
import pta.set.HybridPointsToSet
import soot.SceneTransformer
import sun.rmi.runtime.Log
import util.AnalysisException
import util.sequenceToString

object PointerAnalysisTransformer : SceneTransformer() {
    var output = true
    private const val TAG = "PointerAnalysis"


    override fun internalTransform(p0: String?, p1: MutableMap<String, String>?) {
        val factory = HybridPointsToSet.Factory()
        val contextSelector = SelectorFactory.getContextSelector(p1?.get("cs"), p1?.get("k")?.toInt() ?: 0)
        val pta = PointerAnalysisImpl(
            AllocationSiteBasedModel(),
            JimpleProgramManager(),
            factory,
            MapBasedDataManager(factory),
            contextSelector
        )
        pta.solve()
        JimplePointerAnalysis.setPointerAnalysis(pta)
        if (output) {
            Logger.i(TAG, "----- Reachable methods: -----")
            pta.callGraph.getReachableMethods()
                .asSequence()
                .sortedWith(Comparator.comparing {
                    it.toString()
                })
                .forEach {
                    Logger.i(TAG, it.toString())
                }
            Logger.i(TAG, "----- Call graph edges: -----")
            pta.callGraph.getAllEdges().forEach {
                Logger.i(TAG, it.toString())
            }
            printVariables(pta.variables)
            printInstanceFields(pta.instanceFields)
            Logger.i(TAG, "---------------")
        }
    }

    private fun printVariables(vars: Sequence<CSVariable>) {
        Logger.i(TAG, "----- Points-to sets of all variables: -----")
        vars.sortedWith(Comparator.comparing {
            it.toString()
        }).forEach {
            printPointsToSet(it)
        }
    }

    private fun printInstanceFields(fields: Sequence<InstanceField>) {
        Logger.i(TAG, "----- Points-to sets of all instance fields: -----")
        fields.sortedWith(Comparator.comparing {
            it.toString()
        }).forEach {
            printPointsToSet(it)
        }
    }

    private fun printPointsToSet(pointer: Pointer) {
        Logger.i(TAG, pointer.pointsToSet?.sequence()?.sequenceToString() ?: "null")
    }
}