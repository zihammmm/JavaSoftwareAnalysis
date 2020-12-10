package dataflow.analysis

interface DataFlowAnalysis<Domain, Node> {
    fun isForward(): Boolean

    fun getEntryInitialFlow(node: Node): Domain

    fun newInitialFlow(): Domain

    fun meet(v1: Domain, v2: Domain): Domain

    fun transfer(node: Node, v1: Domain, v2: Domain): Boolean
}