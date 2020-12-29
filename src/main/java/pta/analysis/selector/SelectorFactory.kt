package pta.analysis.selector

import pta.analysis.context.ContextInsensitiveSelector
import util.AnalysisException

object SelectorFactory {
    fun getContextSelector(option: String?, k: Int): ContextSelector {
        if (k == 0) {
            return ContextInsensitiveSelector()
        }
        return when(option) {
            "ci" -> ContextInsensitiveSelector()
            "call", "cfa" -> CallSiteSelector(k)
            "obj", "object" -> ObjectSelector(k)
            "type" -> TypeSelector(k)
            else -> throw AnalysisException("Unknown context sensitivity variant: $option")
        }
    }
}