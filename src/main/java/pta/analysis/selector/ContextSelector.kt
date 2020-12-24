package pta.analysis.selector

import pta.analysis.context.*
import pta.element.Method

interface ContextSelector {

    fun getDefaultContext() = DefaultContext

    fun selectContext(callSite: CallsiteSensitive, callee: Method): IContext

    fun selectContext(callSite: CallsiteSensitive, recv: ObjectSensitive, callee: Method): IContext

    fun selectHeapContext(method: MethodSensitive, allocationSite: Any): IContext
}