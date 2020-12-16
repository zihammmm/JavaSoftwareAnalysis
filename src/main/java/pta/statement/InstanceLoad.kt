package pta.statement

import pta.element.Field
import pta.element.Variable

class InstanceLoad constructor(
    private val to: Variable,
    private val base: Variable,
    private val field: Field
): Statement {

    override val kind: Statement.Kind
        get() = Statement.Kind.INSTANCE_LOAD

    fun getTo() = to

    fun getBase() = base

    fun getField() = field

    override fun toString(): String {
        return "$to = $base.$field"
    }
}