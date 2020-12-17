package pta.analysis.ci

import pta.element.Field
import pta.element.Obj
import pta.element.Variable

class PointerFlowGraph {
    /**
     * Set of all pointers in this PFG
     */
    private val pointer = HashSet<Pointer>()

    /**
     * Map from (Obj, Field) to InstanceField node
     */
    private val instanceField = HashMap<Obj, Map<Field, InstanceField>>()

    /**
     * Map from Variable to Var node
     */
    private val vars = HashMap<Variable, Var>()

    /**
     * Map from a pointer(node) to its successors
     */
    private val graph = HashMap<Pointer, HashSet<Pointer>>()

    fun getVar(variable: Variable): Var {
        return vars.getOrPut(variable) {
            val ret = Var(variable)
            pointer.add(ret)
            ret
        }
    }

    fun getPointer(): Set<Pointer> {
        return pointer
    }

    fun getInstanceFiled(obj: Obj, field: Field): InstanceField {
        return instanceField.getOrPut(obj){
            HashMap()
        }.getOrElse(field) {
            val f = InstanceField(obj, field)
            pointer.add(f)
            f
        }
    }

    fun addEdge(from: Pointer, to: Pointer): Boolean {
        return graph.getOrPut(from) {
            HashSet()
        }.add(to)
    }

    override fun toString(): String {
        return "PointerFlowGraph {graph= $graph }"
    }
}