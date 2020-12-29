package pta.analysis.selector

import pta.analysis.context.*
import pta.analysis.data.CSCallSite
import pta.analysis.data.CSMethod
import pta.analysis.data.CSObj
import pta.element.CallSite
import pta.element.Method

/**
 * k-call-site-sensitivity with k-1 heap context
 */
class CallSiteSelector constructor(
    private val k: Int
): ContextSelector {

    override fun selectContext(callSite: CSCallSite, callee: Method): IContext {
        val context = when(callSite.context.depth) {
            0 -> {
                Context<CallSite>(k)
            }
            else -> {
                Context<CallSite>(callSite.context)
            }
        }
        context.addElement(callSite.callSite)
        return context
    }

    override fun selectContext(callSite: CSCallSite, recv: CSObj, callee: Method): IContext {
        val context = when(callSite.context.depth) {
            0 -> {
                Context<CallSite>(k)
            }
            else -> {
                Context<CallSite>(callSite.context)
            }
        }
        context.addElement(callSite.callSite)
        return context
    }

    override fun selectHeapContext(method: CSMethod, allocationSite: Any): IContext {
        val context = method.context
        return if (context.depth > 1) {
            val newContext = Context<CallSite>(context.depth - 1)
            newContext.addElement(context)
            newContext
        } else {
            context
        }
    }

}