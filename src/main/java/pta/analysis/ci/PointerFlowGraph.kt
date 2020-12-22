package pta.analysis.ci

import pta.element.Field
import pta.element.Obj
import pta.element.Variable

class PointerFlowGraph {
    /**
     * Set of all pointers in this PFG
     */
    private val pointer = HashSet<AbstractPointer>()

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
    private val graph = HashMap<AbstractPointer, HashSet<AbstractPointer>>()

    fun getVar(variable: Variable): Var {
        return vars.getOrPut(variable) {
            val ret = Var(variable)
            pointer.add(ret)
            ret
        }
    }

    fun getPointer(): Set<AbstractPointer> {
        return pointer
    }

    fun getInstanceField(obj: Obj, field: Field): InstanceField {
        return instanceField.getOrPut(obj){
            HashMap()
        }.getOrElse(field) {
            val f = InstanceField(obj, field)
            pointer.add(f)
            f
        }
    }

    fun addEdge(from: AbstractPointer, to: AbstractPointer): Boolean {
        return graph.getOrPut(from) {
            HashSet()
        }.add(to)
    }

    fun getSuccessorsOf(pointer: AbstractPointer): Set<AbstractPointer> = graph.getOrDefault(pointer, emptySet())

    override fun toString(): String {
        return "PointerFlowGraph {graph= $graph }"
    }
}