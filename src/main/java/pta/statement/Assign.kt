package pta.statement

import pta.element.Variable

class Assign constructor(
    private val to: Variable,
    private val from: Variable
): Statement {

    fun getTo() = to

    fun getFrom() = from

    override val kind: Statement.Kind
        get() = Statement.Kind.ASSIGN

    override fun toString(): String {
        return "$from = $to"
    }
}