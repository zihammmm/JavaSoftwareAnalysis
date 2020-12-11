package dataflow.analysis.solver

import dataflow.analysis.DataFlowAnalysis
import soot.toolkits.graph.DirectedGraph

class WorklistSolver<Domain, Node> constructor(
    analysis: DataFlowAnalysis<Domain, Node>,
    cfg: DirectedGraph<Node>
) : AbstractSolver<Domain, Node>(
    analysis, cfg
) {
    override fun solveFixedPoint(cfg: DirectedGraph<Node>) {
        val worklist = ArrayDeque<Node>()
        worklist.addAll(cfg)
        while (worklist.isNotEmpty()) {
            val node = worklist.removeFirst()
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
            if (analysis.transfer(node, inVar!!, outVar!!)) {
                worklist.addAll(cfg.getSuccsOf(node))
            }
        }
    }
}