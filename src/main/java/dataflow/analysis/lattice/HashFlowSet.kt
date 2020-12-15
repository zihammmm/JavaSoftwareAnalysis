package dataflow.analysis.lattice

class HashFlowSet<E> : HashSet<E>, FlowSet<E> {
    constructor() : super()

    constructor(elements: Collection<E>) : super(elements)

    override fun union(v1: FlowSet<E>): FlowSet<E> {
        addAll(v1)
        return this
    }

    override fun intersect(v1: FlowSet<E>): FlowSet<E> {
        retainAll(v1)
        return this
    }

    override fun duplicate(): FlowSet<E> {
        return HashFlowSet(this)
    }

    override fun setTo(v1: FlowSet<E>): FlowSet<E> {
        clear()
        addAll(v1)
        return this
    }
}