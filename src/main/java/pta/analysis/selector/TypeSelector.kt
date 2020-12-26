package pta.analysis.selector

import pta.analysis.context.IContext
import pta.analysis.data.CSCallsite
import pta.analysis.data.CSMethod
import pta.analysis.data.CSObj
import pta.element.Method

class TypeSelector: ContextSelector {
    override fun selectContext(callSite: CSCallsite, callee: Method): IContext {
        TODO("Not yet implemented")
    }

    override fun selectContext(callSite: CSCallsite, recv: CSObj, callee: Method): IContext {
        TODO("Not yet implemented")
    }

    override fun selectHeapContext(method: CSMethod, allocationSite: Any): IContext {
        TODO("Not yet implemented")
    }
}