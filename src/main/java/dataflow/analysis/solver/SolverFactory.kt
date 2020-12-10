package dataflow.analysis.solver

import dataflow.analysis.DataFlowAnalysis
import soot.toolkits.graph.DirectedGraph

object SolverFactory {
    fun <Domain, Node> newSolver(problem: DataFlowAnalysis<Domain, Node>, cfg: DirectedGraph<Node>) = IterativeSolver(problem, cfg)
}