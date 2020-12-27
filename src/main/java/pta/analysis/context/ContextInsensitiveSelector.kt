package pta.analysis.context

import pta.analysis.data.CSCallSite
import pta.analysis.data.CSMethod
import pta.analysis.data.CSObj
import pta.analysis.selector.ContextSelector
import pta.element.Method

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