package pta.element

import pta.statement.Statement

interface Method {
    val isInstance: Boolean

    val isStatic: Boolean

    val isNative: Boolean

    fun getClassType(): Type

    fun getName(): String

    fun getThis(): Variable

    fun getParameters(): MutableList<Variable>

    fun getReturnVariables(): MutableSet<Variable>

    fun getStatements(): MutableSet<Statement>
}