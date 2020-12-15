package callgraph

import soot.Unit
import soot.jimple.*

val Unit.callKind: CallKind
    get() {
        val invoke = (this as Stmt).invokeExpr
        return invoke.callKind
    }


val InvokeExpr.callKind: CallKind
    get() =  when(this) {
        is InterfaceInvokeExpr -> CallKind.INTERFACE
        is VirtualInvokeExpr -> CallKind.VIRTUAL
        is SpecialInvokeExpr -> CallKind.SPECIAL
        else -> CallKind.STATIC
    }
