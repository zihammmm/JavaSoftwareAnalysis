package pta.analysis.selector

import pta.analysis.context.CallsiteSensitive
import pta.analysis.context.IContext
import pta.analysis.context.MethodSensitive
import pta.analysis.context.ObjectSensitive
import pta.element.Method

class TypeSelector: ContextSelector {
    override fun selectContext(callSite: CallsiteSensitive, callee: Method): IContext {
        TODO("Not yet implemented")
    }

    override fun selectContext(callSite: CallsiteSensitive, recv: ObjectSensitive, callee: Method): IContext {
        TODO("Not yet implemented")
    }

    override fun selectHeapContext(method: MethodSensitive, allocationSite: Any): IContext {
        TODO("Not yet implemented")
    }
}