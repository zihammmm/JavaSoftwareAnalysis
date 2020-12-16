package pta.analysis.ci

import pta.element.Variable

class Var constructor(
    private val variable: Variable
): Pointer() {

    fun getVariable() = variable

    override fun toString(): String {
        return variable.toString()
    }
}