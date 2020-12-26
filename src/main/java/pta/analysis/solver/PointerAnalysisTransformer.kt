package pta.analysis.solver

import pta.analysis.heap.AllocationSiteBasedModel
import pta.jimple.JimplePointerAnalysis
import pta.jimple.JimpleProgramManager
import soot.SceneTransformer

object PointerAnalysisTransformer: SceneTransformer() {
    var output = true

    override fun internalTransform(p0: String?, p1: MutableMap<String, String>?) {
        TODO()
    }
}