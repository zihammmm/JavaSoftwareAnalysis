package pta.analysis.selector

import pta.analysis.context.*
import pta.analysis.data.CSCallsite
import pta.analysis.data.CSMethod
import pta.analysis.data.CSObj
import pta.element.Method

/**
 * k-callsite-sensitivity with heap context
 */
class CallsiteSelector constructor(
    private val k: Int
): ContextSelector {
    private val callsiteSensitive = CallsiteSensitive(k)

    override fun selectContext(callSite: CSCallsite, callee: Method): IContext {
        TODO("Not yet implemented")
    }

    override fun selectContext(callSite: CSCallsite, recv: CSObj, callee: Method): IContext {
        TODO("Not yet implemented")
    }

    override fun selectHeapContext(method: CSMethod, allocationSite: Any): IContext {
        return getDefaultContext()
    }

}