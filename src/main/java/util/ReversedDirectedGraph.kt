package util

import soot.toolkits.graph.DirectedGraph

class  ReversedDirectedGraph<N> constructor(
    private val graph: DirectedGraph<N>
): DirectedGraph<N> {

    override fun iterator(): MutableIterator<N> {
        return graph.iterator()
    }

    override fun getHeads(): MutableList<N> {
        return graph.tails
    }

    override fun getTails(): MutableList<N> {
        return graph.heads
    }

    override fun getPredsOf(p0: N): MutableList<N> {
        return graph.getSuccsOf(p0)
    }

    override fun getSuccsOf(p0: N): MutableList<N> {
        return graph.getPredsOf(p0)
    }

    override fun size(): Int {
        return graph.size()
    }
}