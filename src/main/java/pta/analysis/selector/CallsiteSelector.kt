package pta.analysis.selector

import pta.analysis.context.*
import pta.element.CallSite
import pta.element.Method

/**
 * k-callsite-sensitivity with heap context
 */
class CallsiteSelector constructor(
    private val k: Int
): ContextSelector {
    private val callsiteSensitive = CallsiteSensitive(k)

    override fun selectContext(callSite: CallsiteSensitive, callee: Method): IContext {
        TODO("Not yet implemented")
    }

    override fun selectContext(callSite: CallsiteSensitive, recv: ObjectSensitive, callee: Method): IContext {
        TODO("Not yet implemented")
    }

    override fun selectHeapContext(method: MethodSensitive, allocationSite: Any): IContext {
        return getDefaultContext()
    }

}