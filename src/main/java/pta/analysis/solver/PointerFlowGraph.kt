package pta.analysis.solver

import pta.analysis.data.InstanceField
import pta.analysis.data.Pointer
import pta.element.Field
import pta.element.Obj
import pta.element.Type
import pta.element.Variable
import java.util.*
import java.util.function.Function
import java.util.stream.Stream
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class PointerFlowGraph {
    val pointers = HashSet<Pointer>()
    private val edges = HashMap<Pointer, HashSet<PointerFlowEdge>>()
    private val graph = HashMap<Pointer, HashSet<Pointer>>()

    fun addEdge(from: Pointer, to: Pointer, kind: PointerFlowEdge.Kind, type: Type? = null): Boolean {
        return if (!graph.getOrPut(from) {
                HashSet()
            }.contains(to)) {
            graph[from]!!.add(to)
            edges.getOrPut(from){
                HashSet()
            }.add(PointerFlowEdge(kind, from, to, type))
            pointers.add(from)
            pointers.add(to)
            true
        } else {
            false
        }
    }

    fun getOutEdgesOf(pointer: Pointer): Set<PointerFlowEdge> {
        return edges.getOrDefault(pointer, emptySet())
    }

    fun getEdges(): Iterator<PointerFlowEdge> {
        return edges.values.asSequence()
            .flatMap { it.asSequence() }
            .iterator()

    }
}