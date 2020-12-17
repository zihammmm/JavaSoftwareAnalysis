package pta.jimple

import pta.element.Method
import pta.element.Type
import pta.element.Variable
import pta.statement.Statement
import soot.SootMethod

data class JimpleMethod(
    val method: SootMethod,
    val classType: JimpleType
): Method {
    companion object {
        private const val initialCapacity = 4
    }
    private var thisVar: JimpleVariable? = null
    private var parameters = mutableListOf<Variable>()
    private var returnVars = mutableSetOf<Variable>()
    private var statements = mutableSetOf<Statement>()

    override val isInstance: Boolean
        get() = !method.isStatic

    override val isStatic: Boolean
        get() = method.isStatic

    override val isNative: Boolean
        get() = method.isNative

    override fun getClassType(): Type {
        return classType
    }

    override fun getName(): String {
        return method.name
    }

    override fun getThis(): Variable {
        return thisVar!!
    }

    override fun getParameters(): MutableList<Variable> {
        return parameters
    }

    override fun getReturnVariables(): MutableSet<Variable> {
        return returnVars
    }

    override fun getStatements(): MutableSet<Statement> {
        return statements
    }

    fun addReturnVar(returnVar: JimpleVariable) {
        if (returnVars.isEmpty()) {
            returnVars = HashSet(initialCapacity)
        }
        returnVars.add(returnVar)
    }

    fun addStatement(statement: Statement) {
        if (statements.isEmpty()) {
            statements = HashSet(initialCapacity shl 1)
        }
        statements.add(statement)
    }

    override fun equals(other: Any?): Boolean {
        return if (this === other) {
            true
        } else if (other != null && this::class == other::class) {
            return method == (other as JimpleMethod).method
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return method.hashCode()
    }

    override fun toString(): String {
        return method.toString()
    }

}
