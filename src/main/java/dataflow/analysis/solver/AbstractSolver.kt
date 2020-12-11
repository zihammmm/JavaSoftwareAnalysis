package dataflow.analysis.solver

import dataflow.analysis.DataFlowAnalysis
import soot.toolkits.graph.DirectedGraph
import util.ReversedDirectedGraph

abstract class AbstractSolver<Domain, Node> constructor(
    protected val analysis: DataFlowAnalysis<Domain, Node>,
    protected var cfg: DirectedGraph<Node>
) {
    protected val inFlow: MutableMap<Node, Domain> = LinkedHashMap()
    protected val outFlow: MutableMap<Node, Domain> = LinkedHashMap()

    init {
        if (!analysis.isForward()) {
            cfg = ReversedDirectedGraph(cfg)
        }
    }

    fun solve() {
        initialize(cfg)
        solveFixedPoint(cfg)
    }

    val beforeFlow =
        if (analysis.isForward()) {
            inFlow
        } else {
            outFlow
        }

    val afterFlow =
        if (analysis.isForward()) {
            outFlow
        } else {
            inFlow
        }


    protected fun initialize(cfg: DirectedGraph<Node>) {
        for (node in cfg) {
            if (cfg.heads.contains(node)) {
                inFlow[node] = analysis.getEntryInitialFlow(node)
            }
            outFlow[node] = analysis.newInitialFlow()
        }
    }

    protected abstract fun solveFixedPoint(cfg: DirectedGraph<Node>)
}