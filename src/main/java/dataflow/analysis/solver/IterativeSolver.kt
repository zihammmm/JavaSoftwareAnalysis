package dataflow.analysis.solver

import dataflow.analysis.DataFlowAnalysis
import soot.toolkits.graph.DirectedGraph

class IterativeSolver<Domain, Node> constructor(
    analysis: DataFlowAnalysis<Domain, Node>,
    cfg: DirectedGraph<Node>
) : AbstractSolver<Domain, Node>(
    analysis, cfg
) {

    override fun solveFixedPoint(cfg: DirectedGraph<Node>) {
        do {
            var changed = false
            for (node in cfg) {
                var inVar: Domain?
                if (cfg.heads.contains(node)) {
                    inVar = inFlow[node]
                } else {
                    inVar = cfg.getPredsOf(node)
                        .stream()
                        .map(outFlow::get)
                        .reduce(analysis.newInitialFlow()){sum, succ ->
                            analysis.meet(sum!!, succ!!)
                        }

                    inFlow[node] = inVar!!
                }
                val outVar = outFlow[node]
                changed = changed or analysis.transfer(node, inVar!!, outVar!!)
            }
        }while (changed)
    }
}
