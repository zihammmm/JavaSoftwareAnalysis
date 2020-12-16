package pta.statement

import pta.element.Type
import pta.element.Variable

class Allocation constructor(
    private var variable: Variable,
    private var allocationSite: Any,
    private var type: Type
): Statement {
    fun getVar() = variable

    fun getAllocationSite() = allocationSite

    fun getType() = type

    override val kind: Statement.Kind
        get() = Statement.Kind.ALLOCATION

    override fun toString(): String {
        return "$variable = $allocationSite"
    }
}