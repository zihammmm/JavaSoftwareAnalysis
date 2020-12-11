package callgraph

interface CallGraph<CallSite, Method>: Iterable<Edge<CallSite, Method>> {
    fun getCallees(callSite: CallSite): Collection<Method>

    fun getCallers(method: Method): Collection<CallSite>

    fun getContainerMethodOf(callSite: CallSite): Method?

    fun getCallSitesIn(method: Method): Collection<CallSite>

    fun getEdgesOf(callSite: CallSite): Collection<Edge<CallSite, Method>>

    fun getAllEdges(): Sequence<Edge<CallSite, Method>>

    fun getEntryMethods(): Collection<Method>

    fun getReachableMethods(): Collection<Method>

    fun contains(method: Method): Boolean
}