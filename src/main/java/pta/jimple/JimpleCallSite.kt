package pta.jimple

import callgraph.CallKind
import pta.element.CallSite
import pta.element.Method
import pta.element.Variable
import pta.statement.Call
import soot.jimple.Stmt

data class JimpleCallSite(
    val stmt: Stmt,
    val kind: CallKind
) : CallSite {

    override val isInterface: Boolean
        get() = kind == CallKind.INTERFACE

    override val isVirtual: Boolean
        get() = kind == CallKind.VIRTUAL

    override val isSpecial: Boolean
        get() = kind == CallKind.SPECIAL

    override val isStatic: Boolean
        get() = kind == CallKind.STATIC

    override var call: Call? = null
        set(value) {
            field = value
            if (!isStatic) {
                if (value != null) {
                    (receiver as JimpleVariable).addCall(value)
                }
            }
        }

    override var method: Method? = null

    override var receiver: Variable? = null
        set(value) {
            if (value is JimpleVariable) {
                field = value
            }
        }

    val sootInvokeExpr = stmt.invokeExpr

    override var arguments: MutableList<Variable> = mutableListOf()

    override var containerMethod: Method? = null

    override fun equals(other: Any?): Boolean {
        return if (this === other) {
            true
        } else if (other != null && this::class == other::class) {
            return stmt == (other as JimpleCallSite).stmt
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return stmt.hashCode()
    }

    override fun toString(): String {
        return stmt.toString()
    }
}
