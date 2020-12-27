package pta.analysis.selector

import pta.analysis.context.*
import pta.analysis.data.CSCallSite
import pta.analysis.data.CSMethod
import pta.analysis.data.CSObj
import pta.element.Method

/**
 * k-callsite-sensitivity with heap context
 */
class CallSiteSelector constructor(
    private val k: Int
): ContextSelector {
    private val callSiteSensitive = CallsiteSensitive(k)

    override fun selectContext(callSite: CSCallSite, callee: Method): IContext {
        TODO("Not yet implemented")
    }

    override fun selectContext(callSite: CSCallSite, recv: CSObj, callee: Method): IContext {
        TODO("Not yet implemented")
    }

    override fun selectHeapContext(method: CSMethod, allocationSite: Any): IContext {
        return getDefaultContext()
    }

}