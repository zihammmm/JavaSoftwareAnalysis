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
        do {
            for (node in cfg) {
                val inVar =
                        if (cfg.heads.contains(node)) {
                            inFlow[node]
                        } else {
                            cfg.getPredsOf(node).asSequence()
                                    .map {
                                        outFlow[it]
                                    }
                                    .reduce { acc, domain ->
                                        analysis.meet(acc!!, domain!!)
                                    }
                        }
                inFlow[node] = inVar!!
                val outVar = outFlow[node]
                changed = changed or analysis.transfer(node, inVar, outVar!!)
            }
        }while (changed)
    }
}