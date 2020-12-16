package pta.statement

import pta.element.CallSite
import pta.element.Variable

class Call constructor(
    private val callSite: CallSite,
    private val lhs: Variable?
): Statement {

    fun getCallSite() = callSite

    fun getLHS() = lhs

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