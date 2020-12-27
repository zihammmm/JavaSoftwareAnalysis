package pta.statement

import pta.element.Variable

data class Assign constructor(
    val to: Variable,
    val from: Variable
): Statement {

    override val kind: Statement.Kind
        get() = Statement.Kind.ASSIGN

    override fun toString(): String {
        return "$from = $to"
    }
}