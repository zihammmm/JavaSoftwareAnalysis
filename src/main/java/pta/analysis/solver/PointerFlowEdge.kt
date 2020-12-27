package pta.analysis.solver

import pta.analysis.data.Pointer
import pta.element.Type

data class PointerFlowEdge(
    val kind: Kind,
    val from: Pointer,
    val to: Pointer,
    val type: Type?
) {
    override fun equals(other: Any?): Boolean {
        return if (other === this) {
            true
        } else if (other != null && other is PointerFlowEdge) {
            kind == other.kind && from == other.from
        } else {
            false
        }
    }


    override fun hashCode(): Int {
        return arrayOf(kind, from, to, type).hashCode()
    }

    override fun toString(): String {
        return "[$kind]$from->$to"
    }
    enum class Kind {
        LOCAL_ASSIGN,
        INSTANCE_LOAD,
        INSTANCE_STORE,
        PARAMETER_PASSING,
        RETURN
    }
}
