package pta.analysis.solver

import callgraph.Edge
import pta.analysis.data.CSCallSite
import pta.analysis.data.CSMethod
import pta.analysis.data.Pointer
import pta.element.CallSite
import pta.element.Method
import pta.set.PointsToSet

class WorkList {
    private val pointerEntries = ArrayDeque<Entry>()
    private val callEdges = LinkedHashSet<Edge<CSCallSite, CSMethod>>()

    val hasPointerEntries = pointerEntries.isNotEmpty()
    val hasCallEdges = callEdges.isNotEmpty()
    val isEmpty = !hasPointerEntries && !hasCallEdges

    fun addPointerEntry(pointer: Pointer, pointsToSet: PointsToSet) {
        pointerEntries.add(Entry(pointer, pointsToSet))
    }

    fun addPointerEntry(entry: Entry) {
        pointerEntries.add(entry)
    }

    fun pollPointerEntry() = pointerEntries.removeFirst()

    fun addCallEdge(edge: Edge<CSCallSite, CSMethod>) = callEdges.add(edge)

    fun pollCallEdge(): Edge<CSCallSite, CSMethod> {
        val edge = callEdges.iterator().next()
        callEdges.remove(edge)
        return edge
    }

    override fun toString(): String {
        return "WorkList{ pointerEntries = $pointerEntries, callEdges = $callEdges}"
    }

    data class Entry constructor(
        val pointer: Pointer,
        val pointsToSet: PointsToSet
    )
}