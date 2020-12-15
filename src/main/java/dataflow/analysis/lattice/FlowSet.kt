package dataflow.analysis.lattice

interface FlowSet<E>: MutableSet<E> {
    fun union(v1: FlowSet<E>): FlowSet<E>

    fun intersect(v1: FlowSet<E>): FlowSet<E>

    fun duplicate(): FlowSet<E>

    fun setTo(v1: FlowSet<E>): FlowSet<E>
}