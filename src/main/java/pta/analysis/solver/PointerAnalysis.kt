package pta.analysis.solver

import base.IAnalysis
import callgraph.CallGraph
import pta.analysis.ProgramManager
import pta.analysis.data.*
import pta.analysis.heap.HeapModel
import pta.analysis.selector.ContextSelector
import pta.set.PointsToSetFactory

interface PointerAnalysis: IAnalysis {
    var programManager: ProgramManager

    var dataManager: DataManager

    var contextSelector: ContextSelector

    var heapModel: HeapModel

    var pointsToSetFactory: PointsToSetFactory

    var contextSensitive: Boolean

    val callGraph: CallGraph<CSCallSite, CSMethod>

    val variables: Sequence<CSVariable>

    val instanceFields: Sequence<InstanceField>

}