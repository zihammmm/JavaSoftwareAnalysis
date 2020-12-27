package pta.statement

import pta.element.Obj
import pta.element.Type
import pta.element.Variable

data class Allocation constructor(
    val variable: Variable,
    val obj: Obj
): Statement {

    override val kind: Statement.Kind
        get() = Statement.Kind.ALLOCATION

    override fun toString(): String {
        return "$variable = $obj"
    }
}