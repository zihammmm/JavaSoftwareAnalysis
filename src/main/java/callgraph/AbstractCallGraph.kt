package callgraph

import util.CollectionView
import util.addToMapSet
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

abstract class AbstractCallGraph<CallSite, Method> : CallGraph<CallSite, Method> {
    protected val callSiteToEdges = HashMap<CallSite, MutableSet<Edge<CallSite, Method>>>()
    protected val calleeToEdges = HashMap<Method, MutableSet<Edge<CallSite, Method>>>()
    protected val callSiteToContainer = HashMap<CallSite, Method>()
    protected val callSiteIn = HashMap<Method, MutableSet<CallSite>>()
    protected val entryMethods = HashSet<Method>()
    protected val reachableMethods = HashSet<Method>()

    fun addEntryMethod(entryMethod: Method) {
        entryMethods.add(entryMethod)
        addNewMethod(entryMethod)
    }

    fun addEdge(callSite: CallSite, method: Method, kind: CallKind): Boolean {
        addNewMethod(method)
        val edge = Edge(kind, callSite, method)
        return callSiteToEdges.addToMapSet(callSite, edge) or calleeToEdges.addToMapSet(method, edge)
    }

    protected abstract fun addNewMethod(method: Method): Boolean

    override fun getCallees(callSite: CallSite): Collection<Method> {
        val edges = callSiteToEdges[callSite]
        return if (edges != null) {
            CollectionView.of(edges, Edge<CallSite, Method>::callee)
        } else {
            Collections.emptySet()
        }
    }

    override fun getCallers(method: Method): Collection<CallSite> {
        val edges = calleeToEdges[method]
        return if (edges != null) {
            CollectionView.of(edges, Edge<CallSite, Method>::callSite)
        } else {
            Collections.emptySet()
        }
    }

    override fun getContainerMethodOf(callSite: CallSite): Method? {
        return callSiteToContainer[callSite]
    }

    override fun getCallSitesIn(method: Method): Collection<CallSite> {
        return callSiteIn.getOrDefault(method, Collections.emptySet())
    }

    override fun getEdgesOf(callSite: CallSite): Collection<Edge<CallSite, Method>> {
        return callSiteToEdges.getOrDefault(callSite, Collections.emptySet())
    }

    override fun getAllEdges(): Sequence<Edge<CallSite, Method>> {
        return callSiteToEdges.values.asSequence().flatMap {
            it.asIterable()
        }
    }

    override fun getEntryMethods(): Collection<Method> {
        return entryMethods
    }

    override fun getReachableMethods(): Collection<Method> {
        return reachableMethods
    }

    override fun contains(method: Method): Boolean {
        return reachableMethods.contains(method)
    }

    override fun iterator(): Iterator<Edge<CallSite, Method>> {
        return getAllEdges().iterator()
    }
}