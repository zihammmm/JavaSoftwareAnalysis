package pta.analysis.selector

import pta.analysis.context.*
import pta.analysis.data.CSCallSite
import pta.analysis.data.CSMethod
import pta.analysis.data.CSObj
import pta.element.Method

interface ContextSelector {

    fun getDefaultContext() = DefaultContext

    fun selectContext(callSite: CSCallSite, callee: Method): IContext

    fun selectContext(callSite: CSCallSite, recv: CSObj, callee: Method): IContext

    fun selectHeapContext(method: CSMethod, allocationSite: Any): IContext
}