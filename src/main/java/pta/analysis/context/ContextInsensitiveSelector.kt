package pta.analysis.context

import pta.analysis.data.CSCallSite
import pta.analysis.data.CSMethod
import pta.analysis.data.CSObj
import pta.analysis.selector.ContextSelector
import pta.element.Method

/**
 * Context-InSensitive
 * the depth is 0
 */
object DefaultContext: IContext {
    override val depth: Int
        get() = 0
}

class ContextInsensitiveSelector: ContextSelector {
    override fun selectContext(callSite: CSCallSite, callee: Method): IContext {
        return DefaultContext
    }

    override fun selectContext(callSite: CSCallSite, recv: CSObj, callee: Method): IContext {
        return DefaultContext
    }

    override fun selectHeapContext(method: CSMethod, allocationSite: Any): IContext {
        return DefaultContext
    }
}