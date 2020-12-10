package base.callgraph

import java.util.*

data class Edge<CallSite, Method>(
    val kind: CallKind,
    val callSite: CallSite,
    val callee: Method
) {
    private val hashCode = Objects.hash(kind, callSite, callee)

    override fun equals(other: Any?): Boolean {
        return if (this === other) {
            true
        } else if (other != null && other::class.java == this::class.java) {
            val edge = other as Edge<*, *>
            return this.kind == edge.kind && this.callSite == edge.callSite && this.callee == edge.callee
        }else {
            return false
        }
    }

    override fun hashCode() = hashCode

    override fun toString(): String {
        return "[$kind]$callSite->$callee"
    }
}
