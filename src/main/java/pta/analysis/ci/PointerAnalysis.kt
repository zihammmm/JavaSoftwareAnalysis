package pta.analysis.ci

import base.IAnalysis
import pta.analysis.heap.HeapModel

class PointerAnalysis: IAnalysis {

    private lateinit var callGraph: OnFlyCallGraph

    private lateinit var workList: WorkList

    private lateinit var heapModel: HeapModel

    private lateinit var pointerFlowGraph: PointerFlowGraph

    fun getVariables(): Sequence<Var> {
        return pointerFlowGraph.getPointer().asSequence()
            .filter { it is Var }
            .map { it as Var }
    }

    fun getInstanceFields(): Sequence<InstanceField> {
        return pointerFlowGraph.getPointer().asSequence()
            .filter { it is InstanceField }
            .map { it as InstanceField }
    }

    override fun initialize() {
        callGraph = OnFlyCallGraph()
        pointerFlowGraph = PointerFlowGraph()
        workList = WorkList()

    }

    override fun solve() {
        initialize()
    }

    private fun analyze() {

    }
}