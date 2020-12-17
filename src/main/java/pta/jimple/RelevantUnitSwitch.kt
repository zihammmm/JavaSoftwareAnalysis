package pta.jimple

import soot.jimple.*
import soot.options.Options

class RelevantUnitSwitch : StmtSwitch {
    var relevant = true
    private set

    private fun isNotPhantom(invoke: InvokeExpr) = !invoke.methodRef.declaringClass.isPhantom

    override fun caseBreakpointStmt(p0: BreakpointStmt?) {
        TODO("Not yet implemented")
    }

    override fun caseInvokeStmt(p0: InvokeStmt?) {
        TODO("Not yet implemented")
    }

    override fun caseAssignStmt(p0: AssignStmt?) {
        relevant = if (Options.v().allow_phantom_refs()) {
            val right = p0?.rightOp
            if (right is InvokeExpr) {
                isNotPhantom(right)
            } else {
                true
            }
        } else {
            true
        }
    }

    override fun caseIdentityStmt(p0: IdentityStmt?) {
        TODO("Not yet implemented")
    }

    override fun caseEnterMonitorStmt(p0: EnterMonitorStmt?) {
        TODO("Not yet implemented")
    }

    override fun caseExitMonitorStmt(p0: ExitMonitorStmt?) {
        TODO("Not yet implemented")
    }

    override fun caseGotoStmt(p0: GotoStmt?) {
        TODO("Not yet implemented")
    }

    override fun caseIfStmt(p0: IfStmt?) {
        TODO("Not yet implemented")
    }

    override fun caseLookupSwitchStmt(p0: LookupSwitchStmt?) {
        TODO("Not yet implemented")
    }

    override fun caseNopStmt(p0: NopStmt?) {
        TODO("Not yet implemented")
    }

    override fun caseRetStmt(p0: RetStmt?) {
        TODO("Not yet implemented")
    }

    override fun caseReturnStmt(p0: ReturnStmt?) {
        TODO("Not yet implemented")
    }

    override fun caseReturnVoidStmt(p0: ReturnVoidStmt?) {
        TODO("Not yet implemented")
    }

    override fun caseTableSwitchStmt(p0: TableSwitchStmt?) {
        TODO("Not yet implemented")
    }

    override fun caseThrowStmt(p0: ThrowStmt?) {
        TODO("Not yet implemented")
    }

    override fun defaultCase(p0: Any?) {
        TODO("Not yet implemented")
    }
}