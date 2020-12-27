package pta.statement

import pta.element.CallSite
import pta.element.Variable

data class Call constructor(
    val callSite: CallSite,
    val lhs: Variable?
): Statement {

    override val kind: Statement.Kind
        get() = Statement.Kind.CALL

    override fun toString(): String {
        return if (lhs != null) {
            "$lhs = $callSite"
        } else {
            callSite.toString()
        }
    }
}