package pta.statement

import pta.element.Type
import pta.element.Variable

class Allocation constructor(
    val variable: Variable,
    val allocationSite: Any,
    val type: Type
): Statement {

    override val kind: Statement.Kind
        get() = Statement.Kind.ALLOCATION

    override fun toString(): String {
        return "$variable = $allocationSite"
    }
}