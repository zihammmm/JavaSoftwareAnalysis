package pta.jimple

import soot.*
import soot.jimple.NullConstant
import util.AnalysisException

class ElementManager {
    private val methods = HashMap<SootMethod, JimpleMethod>()
    private val types = HashMap<Type, JimpleType>()
    private val vars = HashMap<JimpleMethod, HashMap<Local, JimpleVariable>>()
    private val fields = HashMap<SootField, JimpleField>()
    private val methodBuilder = MethodBuilder()
    private val varManager = NewVariableManager()

    fun getMethod(method: SootMethod): JimpleMethod {
        return methods.getOrPut(method) {
            createMethod(method)
        }
    }

    private fun getType(sootClass: SootClass) :JimpleType {
        return types.getOrPut(sootClass.type) {
            JimpleType(sootClass.type)
        }
    }

    private fun getType(type: Type): JimpleType {
        return types.getOrPut(type) {
            JimpleType(type)
        }
    }

    private fun getField(sootField: SootField): JimpleField {
        return fields.getOrPut(sootField) {
            JimpleField(sootField, getType(sootField.declaringClass), getType(sootField.type))
        }
    }

    fun getLocalVariablesOf(container: JimpleMethod): Collection<JimpleVariable> {
        return vars[container]!!.values
    }

    fun getVariable(variable: Local, container: JimpleMethod): JimpleVariable? {
        return if (variable.type is RefLikeType) {
            vars.getOrPut(container) {
                HashMap()
            }.getOrPut(variable) {
                JimpleVariable(variable, getType(variable.type), container)
            }
        } else {
            null
        }
    }

    fun getVariable(value: Value, container: JimpleMethod): JimpleVariable? {
        return when(value) {
            is Local -> getVariable(value, container)
            is NullConstant -> varManager.getTempVariable("null$", getType(value.type), container)
            else -> throw AnalysisException("Cannot handle value: $value")
        }
    }

    private fun createMethod(method: SootMethod): JimpleMethod {
        val jType =
    }

    private class MethodBuilder {

    }

    private class NewVariableManager {
        fun getTempVariable(baseName: String, type: JimpleType, container: JimpleMethod): JimpleVariable {

        }
    }
}