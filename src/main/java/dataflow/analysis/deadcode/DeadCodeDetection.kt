package dataflow.analysis.deadcode

import dataflow.analysis.constprop.ConstantPropagation
import dataflow.analysis.constprop.FlowMap
import dataflow.analysis.lattice.DataFlowTag
import dataflow.analysis.lattice.FlowSet
import logger.Logger
import soot.*
import soot.Unit
import soot.jimple.*
import soot.toolkits.graph.BriefUnitGraph
import soot.toolkits.graph.DirectedGraph
import util.unitToString
import java.util.Comparator

object DeadCodeDetection: BodyTransformer() {
    private const val CONDITION_TRUE = 1
    private const val CONDITION_FALSE = 0
    private const val TAG = "DeadCodeDetection"

    var isOutput = true

    /**
     * 找到不可达分支，再遍历标记所有可达语句，剩下的就是DeadCode
     */
    private fun findDeadCode(b: Body): Set<Unit> {
        val cfg = BriefUnitGraph(b)
        val unreachableCodeBranches = findUnreachableBranches(b, cfg)
        val deadCode = HashSet<Unit>(findUnreachableCode(cfg, unreachableCodeBranches))
        deadCode.addAll(findDeadAssignments(b))

        return deadCode
    }

    private fun findUnreachableBranches(body: Body, cfg: DirectedGraph<Unit>): EdgeSet {
        @Suppress("unchecked")
        val constantTag = body.getTag("ConstantTag") as DataFlowTag<Unit, FlowMap>
        val constantMap = constantTag.dataFlowMap
        val unreachableBranches = EdgeSet()
        for (unit in cfg) {
            if (unit is IfStmt) {
                val result = ConstantPropagation.computeValue(unit.condition, constantMap[unit]!!)
                if (result.isConstant) {
                    if (result.getConstant() == CONDITION_TRUE) {
                        unreachableBranches.addEdge(unit, body.units.getSuccOf(unit))
                    } else {
                        unreachableBranches.addEdge(unit, unit.target)
                    }
                }else {
                    Logger.d(TAG, "result is $result")
                }
            }
        }
        return unreachableBranches
    }

    /**
     * 遍历所有语句，将可达语句过滤后剩下的就是不可达语句
     */
    private fun findUnreachableCode(cfg: DirectedGraph<Unit>, filterEdges: EdgeSet): Set<Unit> {
        val deque = ArrayDeque<Unit>()
        deque.add(getEntry(cfg)!!)
        val visited = HashSet<Unit>()
        val reachable = HashSet<Unit>()
        while (deque.isNotEmpty()) {
            val parent = deque.removeFirst()
            if (!visited.contains(parent)) {
                reachable.add(parent)
                visited.add(parent)
                for (child in cfg.getSuccsOf(parent)) {
                    if (!filterEdges.containsEdge(parent, child)) {
                        deque.addFirst(child)
                    }
                }
            }
        }
        val ret = HashSet<Unit>()
        for (unit in cfg) {
            if (!reachable.contains(unit)) {
                ret.add(unit)
            }
        }
        return ret
    }

    private fun findDeadAssignments(body: Body): Set<Unit> {
        @Suppress("unchecked")
        val liveVarTag = body.getTag("LiveVarTag") as DataFlowTag<Unit, FlowSet<Local>>
        val liveVarMap = liveVarTag.dataFlowMap
        val deadAssigns = HashSet<Unit>()
        for (unit in body.units) {
            if (unit is AssignStmt) {
                val left = unit.leftOp as Local
                liveVarMap[unit]?.let {
                    if (!it.contains(left) && !mayHaveSideEffect(unit)) {
                        deadAssigns.add(unit)
                    }
                }
            }
        }
        return deadAssigns
    }

    private fun mayHaveSideEffect(assignStmt: AssignStmt): Boolean {
        return when(assignStmt.rightOp) {
            is InvokeExpr, is AnyNewExpr, is CastExpr, is ConcreteRef, is DivExpr, is RemExpr -> true
            else -> false
        }
    }

    /**
     * 如果有不可达语句，则cfg是由多棵树组成的森林，entry必须是在源码的开头
     */
    private fun getEntry(cfg: DirectedGraph<Unit>): Unit? {
        return cfg.heads
            .stream()
            .min(Comparator.comparingInt { obj: Unit -> obj.javaSourceStartLineNumber }
                .thenComparingInt { obj: Unit -> obj.javaSourceStartColumnNumber })
            .orElse(null)
    }

    override fun internalTransform(p0: Body?, p1: String?, p2: MutableMap<String, String>?) {
        if (p0 != null) {
            val deadCode = findDeadCode(p0)
            if (isOutput) {
                outputResult(p0, deadCode)
            }
        }
    }

    @Synchronized
    private fun outputResult(body: Body, deadCode: Set<Unit>) {
        val up = BriefUnitPrinter(body)
        body.units
            .asSequence()
            .filter {
                deadCode.contains(it)
            }
            .forEach {
                Logger.d(TAG, unitToString(up, it))
            }
    }

    private class EdgeSet {
        private val edgeSet: MutableSet<Pair<Unit, Unit>> = HashSet()

        fun addEdge(from: Unit, to: Unit) = edgeSet.add(Pair(from, to))

        fun containsEdge(from: Unit, to: Unit) = edgeSet.contains(Pair(from, to))

        override fun toString() = "[$edgeSet]"
    }

}