package dataflow.analysis.constprop

import dataflow.analysis.DataFlowAnalysis
import dataflow.analysis.lattice.DataFlowTag
import dataflow.analysis.solver.AbstractSolver
import dataflow.analysis.solver.SolverFactory
import logger.DefaultLogger
import soot.*
import soot.Unit
import soot.jimple.*
import soot.toolkits.graph.BriefUnitGraph
import util.SootUtils
import util.resultchecker.CPResultChecker

object ConstantPropagation : BodyTransformer(), DataFlowAnalysis<FlowMap, Unit> {
    private const val TAG = "ConstantPropagation"

    var isOutput = true

    override fun internalTransform(p0: Body?, p1: String?, p2: MutableMap<String, String>?) {
        val cfg = BriefUnitGraph(p0)
        val solver = SolverFactory.newWorklistSolver(this, cfg)
        solver.solve()
        p0?.let {
            it.addTag(DataFlowTag("ConstantTag", solver.afterFlow))
            CPResultChecker.compare(it, solver.afterFlow)
        }
        if (isOutput && p0 != null) {
            outputResult(p0, solver.afterFlow)
        }
    }

    override fun isForward() = true

    override fun getEntryInitialFlow(node: Unit) = newInitialFlow()

    override fun meet(v1: FlowMap, v2: FlowMap): FlowMap {
        val result = FlowMap()
        result.copyFrom(v1)
        for (entry in v2.entries) {
            val key = entry.key
            result[key] =
                if (result.containsKey(key)) {
                    meetValue(entry.value, result[key])
                } else {
                    entry.value
                }
        }

        return result
    }

    override fun transfer(node: Unit, v1: FlowMap, v2: FlowMap): Boolean {
        val oldOut = FlowMap()
        oldOut.copyFrom(v2)
        v2.copyFrom(v1)
        if (node is DefinitionStmt) {
            val x = node.leftOp as Local
            val r = node.rightOp
            val temp = computeValue(r, v1)
            v2.update(x, temp)
        }
        return oldOut != v2
    }

    override fun newInitialFlow() = FlowMap()

    private fun meetValue(v1: Value, v2: Value): Value {
        return if (v1.isNAC || v2.isNAC) {
            Value.NAC
        } else if (v1.isUndef || v2.isUndef) {
            if (v1.isUndef) {
                v2
            } else {
                v1
            }
        } else {
            // v1 is Constant or v2 is Constant
            if (v1 == v2) {
                Value.makeConstant(v1.getConstant())
            } else {
                Value.NAC
            }
        }
    }


    private fun computeValue(rhs: soot.Value, inFlowMap: FlowMap): Value {
        return when (rhs) {
            is Local -> inFlowMap[rhs]
            is IntConstant -> Value.makeConstant(rhs.value)
            is BinopExpr -> {
                val l = computeValue(rhs.op1, inFlowMap)
                val r = computeValue(rhs.op2, inFlowMap)
                if (l.isNAC || r.isNAC) {
                    Value.NAC
                } else if (l.isConstant && r.isConstant) {
                    calculateExp(rhs, l, r)
                } else {
                    Value.UNDEF
                }
            }
            else -> Value.NAC
        }
    }

    private fun calculateExp(rhs: soot.Value, l: Value, r: Value): Value {
        val lValue = l.getConstant()
        val rValue = r.getConstant()
        return when (rhs) {
            is AddExpr -> Value.makeConstant(lValue + rValue)
            is SubExpr -> Value.makeConstant(lValue - rValue)
            is MulExpr -> Value.makeConstant(lValue * rValue)
            is DivExpr -> Value.makeConstant(lValue / rValue)
            is EqExpr -> if (lValue == rValue) {
                Value.makeConstant(1)
            } else {
                Value.makeConstant(0)
            }
            is NeExpr -> if (lValue != rValue) {
                Value.makeConstant(1)
            } else {
                Value.makeConstant(0)
            }
            is GeExpr -> if (lValue >= rValue) {
                Value.makeConstant(1)
            } else {
                Value.makeConstant(0)
            }
            is GtExpr -> if (lValue > rValue) {
                Value.makeConstant(1)
            } else {
                Value.makeConstant(0)
            }
            is LeExpr -> if (lValue <= rValue) {
                Value.makeConstant(1)
            } else {
                Value.makeConstant(0)
            }
            is LtExpr -> if (lValue < rValue) {
                Value.makeConstant(1)
            } else {
                Value.makeConstant(0)
            }
            else -> Value.NAC
        }
    }

    @Synchronized
    fun outputResult(body: Body, result: MutableMap<Unit, FlowMap>) {
        val up = BriefUnitPrinter(body)
        body.units.forEach {
            DefaultLogger.d(TAG, "${SootUtils.unitToString(up, it)}:${result[it]}")
        }
    }

}