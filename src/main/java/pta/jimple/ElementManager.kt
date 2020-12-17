package pta.jimple

import callgraph.callKind
import pta.element.Variable
import pta.statement.*
import soot.*
import soot.jimple.*
import soot.jimple.internal.JimpleLocal
import util.AnalysisException
import util.MutableInt

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

    private fun getType(sootClass: SootClass): JimpleType {
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
        return when (value) {
            is Local -> getVariable(value, container)
            is NullConstant -> varManager.getTempVariable("null$", getType(value.type), container)
            else -> throw AnalysisException("Cannot handle value: $value")
        }
    }

    private fun createMethod(method: SootMethod): JimpleMethod {
        val jMethod = JimpleMethod(method, getType(method.declaringClass))
        if (method.isNative) {
            methodBuilder.buildNative(jMethod)
        } else if (!method.isAbstract) {
            methodBuilder.buildConcrete(jMethod, method.retrieveActiveBody())
        }
        return jMethod
    }

    private fun createCallSite(stmt: Stmt, container: JimpleMethod): JimpleCallSite {
        val invoke = stmt.invokeExpr
        val callSite = JimpleCallSite(stmt, invoke.callKind)
        callSite.apply {
            if (invoke is InstanceInvokeExpr) {
                receiver = getVariable(invoke.base, container)
            }
            arguments = invoke.args.asSequence()
                .map {
                    getVariable(it, container)
                }
                .filter {
                    it != null
                }
                .toCollection(mutableListOf())

            containerMethod = container
        }
        return callSite
    }

    private inner class MethodBuilder {
        private val sw = RelevantUnitSwitch()

        fun buildNative(method: JimpleMethod) {
            val sootMethod = method.method
            if (!sootMethod.isStatic) {
                method.setThis(this@ElementManager.varManager.getThisVariable(method))
            }

            val paramCount = sootMethod.parameterCount
            if (paramCount > 0) {
                val params = ArrayList<Variable>(paramCount)
                for (i in 0 until paramCount) {
                    params.add(this@ElementManager.varManager.getParameter(method, i))
                }
                method.setParameters(params)
            }

            if (sootMethod.returnType is RefLikeType) {
                method.addReturnVar(this@ElementManager.varManager.getReturnVariable(method))
            }
        }

        fun buildConcrete(method: JimpleMethod, body: Body) {
            if (!method.isStatic) {
                this@ElementManager.getVariable(body.thisLocal, method)?.let { method.setThis(it) }
            }

            val params = body.parameterLocals.asSequence()
                .map {
                    this@ElementManager.getVariable(it, method)
                }
                .toCollection(mutableListOf())
                .filterNotNull()
                .toMutableList() as MutableList<Variable>
            method.setParameters(params)

            for (unit in body.units) {
                unit.apply(sw)
                if (sw.relevant) {
                    when(unit) {
                        is AssignStmt -> build(method, unit)
                        is InvokeStmt -> build(method, unit)
                        is IdentityStmt -> build(method, unit)
                        is ReturnStmt -> build(method, unit)
                        else -> {
                            if (unit !is ThrowStmt) {
                                throw RuntimeException("Cannot handle statement: $unit")
                            }
                            build(method, unit)
                        }
                    }
                }
            }
        }

        fun build(method: JimpleMethod, stmt: AssignStmt) {
            val left = stmt.leftOp
            val right = stmt.rightOp
            if (left is Local) {
                val lhs = this@ElementManager.getVariable(left, method)
                if (stmt.containsInvokeExpr()) {
                    val callSite = this@ElementManager.createCallSite(stmt, method)
                    val call = Call(callSite, lhs)
                    callSite.call = call
                    method.addStatement(call)
                } else {
                    lhs?.let {
                        when(right) {
                            is NewExpr -> method.addStatement(Allocation(it, stmt, this@ElementManager.getType(right.type)))
                            is Local -> method.addStatement(Assign(it, this@ElementManager.getVariable(right, method)!!))
                            is InstanceFieldRef -> {
                                val base = this@ElementManager.getVariable(right.base, method)!!
                                val load = InstanceLoad(it, base, this@ElementManager.getField(right.field))
                                base.addLoad(load)
                                method.addStatement(load)
                            }
                        }
                    }
                }
            } else if (left is InstanceFieldRef) {
                val base = this@ElementManager.getVariable(left.base, method)!!
                val store = InstanceStore(base, this@ElementManager.getField(left.field), this@ElementManager.getVariable(right, method)!!)
                base.addStore(store)
                method.addStatement(store)
            }
        }

        fun build(method: JimpleMethod, stmt: InvokeStmt) {
            val callSite = this@ElementManager.createCallSite(stmt, method)
            val call = Call(callSite, null)
            callSite.call = call
            method.addStatement(call)
        }

        fun build(method: JimpleMethod, stmt: ReturnStmt) {
            if (stmt.op.type is RefLikeType) {
                method.addReturnVar(this@ElementManager.getVariable(stmt.op, method)!!)
            }
        }

        fun build(method: JimpleMethod, stmt: IdentityStmt) {

        }

        fun build(method: JimpleMethod, stmt: ThrowStmt) {

        }
    }

     private inner class NewVariableManager {
        private val varNumbers = HashMap<JimpleMethod, MutableInt>()

        fun getTempVariable(baseName: String, type: JimpleType, container: JimpleMethod): JimpleVariable {
            return getNewVariable("${baseName}${getNewNumber(container)}", type, container)
        }

        fun getNewNumber(container: JimpleMethod): Int {
            return varNumbers.getOrPut(container) {
                MutableInt(0)
            }.increase()
        }

        fun getThisVariable(container: JimpleMethod): JimpleVariable {
            return getNewVariable("@this", container.classType, container)
        }

        fun getParameter(container: JimpleMethod, index: Int): JimpleVariable {
            val type = this@ElementManager.getType(container.method.parameterTypes[index])
            return getNewVariable("@parameter$index", type, container)
        }

        fun getReturnVariable(container: JimpleMethod): JimpleVariable {
            val type = this@ElementManager.getType(container.method.returnType)
            return getNewVariable("@return", type, container)
        }

        fun getNewVariable(varName: String, type: JimpleType, container: JimpleMethod): JimpleVariable {
            return JimpleVariable(JimpleLocal(varName, type.sootType), type, container)
        }
    }
}