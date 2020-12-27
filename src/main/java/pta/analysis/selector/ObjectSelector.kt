package pta.analysis.selector

import pta.analysis.context.IContext
import pta.analysis.data.CSCallSite
import pta.analysis.data.CSMethod
import pta.analysis.data.CSObj
import pta.element.Method

class ObjectSelector: ContextSelector {
    override fun selectContext(callSite: CSCallSite, callee: Method): IContext {
        TODO("Not yet implemented")
    }

    override fun selectContext(callSite: CSCallSite, recv: CSObj, callee: Method): IContext {
        TODO("Not yet implemented")
    }

    override fun selectHeapContext(method: CSMethod, allocationSite: Any): IContext {
        TODO("Not yet implemented")
    }
}