package pta.analysis.ci

import pta.element.Variable

/**
 * Represents variable nodes in PFG
 */
class Var constructor(
    private val variable: Variable
): Pointer() {

    fun getVariable() = variable

    override fun toString(): String {
        return variable.toString()
    }
}