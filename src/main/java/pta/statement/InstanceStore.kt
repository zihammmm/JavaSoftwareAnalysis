package pta.statement

import pta.element.Field
import pta.element.Variable

class InstanceStore constructor(
    private val base: Variable,
    private val field: Field,
    private val from: Variable
): Statement {
    override val kind: Statement.Kind
        get() = Statement.Kind.INSTANCE_STORE

    fun getBase() = base

    fun getField() = field

    fun getFrom() = from

    override fun toString(): String {
        return "$base.$field = $from"
    }
}