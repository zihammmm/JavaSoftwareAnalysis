package dataflow.analysis.solver

import dataflow.analysis.DataFlowAnalysis
import soot.toolkits.graph.DirectedGraph

object SolverFactory {
    fun <Domain, Node> newIterativeSolver(problem: DataFlowAnalysis<Domain, Node>, cfg: DirectedGraph<Node>) = IterativeSolver(problem, cfg)

    fun <Domain, Node> newWorklistSolver(problem: DataFlowAnalysis<Domain, Node>, cfg: DirectedGraph<Node>) = WorklistSolver(problem, cfg)
}