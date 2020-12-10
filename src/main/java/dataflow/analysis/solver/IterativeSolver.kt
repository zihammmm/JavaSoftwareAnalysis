package dataflow.analysis.solver

import dataflow.analysis.DataFlowAnalysis
import soot.toolkits.graph.DirectedGraph

class IterativeSolver<Domain, Node> constructor(
    analysis: DataFlowAnalysis<Domain, Node>,
    cfg: DirectedGraph<Node>
) : AbstractSolver<Domain, Node>(
    analysis, cfg
) {
    init {
        inFlow = LinkedHashMap()
        outFlow = LinkedHashMap()
    }
    override fun solveFixedPoint(cfg: DirectedGraph<Node>) {
        var changed = false
        for (node in cfg) {
            if (cfg.heads.contains(node)) {
                val inVar = inFlow[node]
            } else {
                val inVar = cfg.getPredsOf(node).asSequence()
                    .map {
                        outFlow[it]
                    }
                    .reduce { acc, domain ->
                        analysis.newInitialFlow()
                    }

            }
        }
    }
}