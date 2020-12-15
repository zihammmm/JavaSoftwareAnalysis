package dataflow.analysis.livevar

import dataflow.analysis.DataFlowAnalysis
import dataflow.analysis.lattice.DataFlowTag
import dataflow.analysis.lattice.FlowSet
import dataflow.analysis.lattice.HashFlowSet
import dataflow.analysis.solver.SolverFactory
import logger.Logger
import soot.*
import soot.Unit
import soot.toolkits.graph.BriefUnitGraph
import util.unitToString

class LiveVariableAnalysis: BodyTransformer(), DataFlowAnalysis<FlowSet<Local>, Unit> {
    companion object {
        const val TAG = "LiveVariableAnalysis"
    }
    var isOutput = true

    override fun internalTransform(p0: Body?, p1: String?, p2: MutableMap<String, String>?) {
        if (p0 != null) {
            val cfg = BriefUnitGraph(p0)
            val solver = SolverFactory.newIterativeSolver(this, cfg)
            solver.solve()
            p0.addTag(DataFlowTag("LiveVarTag", solver.afterFlow))
            if (isOutput) {
                outputResult(p0, solver.afterFlow)
            }
        }
    }

    override fun isForward() = false

    override fun getEntryInitialFlow(node: Unit) = getLocals(node.useBoxes)

    override fun newInitialFlow() = HashFlowSet<Local>()

    override fun meet(v1: FlowSet<Local>, v2: FlowSet<Local>): FlowSet<Local> {
        val result = v1.duplicate()
        return result.union(v2)
    }

    override fun transfer(node: Unit, inDomain: FlowSet<Local>, outDomain: FlowSet<Local>): Boolean {
        val oldOut = outDomain.duplicate()
        val defVariableSet = getLocals(node.defBoxes)
        val useVariableSet = getLocals(node.useBoxes)
        val unionSet = inDomain.duplicate().intersect(defVariableSet)
        outDomain.setTo(inDomain).removeAll(unionSet)
        outDomain.union(useVariableSet)
        return outDomain != oldOut
    }

    private fun getLocals(valueBoxes: List<ValueBox>): FlowSet<Local> {
        val variableSet = HashFlowSet<Local>()
        for (vb in valueBoxes) {
            val value = vb.value
            if (value is Local) {
                variableSet.add(value)
            }
        }
        return variableSet
    }

    @Synchronized
    private fun outputResult(body: Body, result: Map<Unit, FlowSet<Local>>) {
        val up = BriefUnitPrinter(body)
        body.units
            .forEach {
                Logger.d(TAG, "${unitToString(up, it)}: ${result[it]}")
            }
    }
}