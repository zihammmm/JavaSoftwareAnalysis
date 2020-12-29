package pta.analysis.selector

import pta.analysis.context.Context
import pta.analysis.context.IContext
import pta.analysis.data.CSCallSite
import pta.analysis.data.CSMethod
import pta.analysis.data.CSObj
import pta.element.CallSite
import pta.element.Method
import pta.element.Obj

class ObjectSelector constructor(
    private val k: Int
): ContextSelector {
    override fun selectContext(callSite: CSCallSite, callee: Method): IContext {
        return callSite.context
    }

    override fun selectContext(callSite: CSCallSite, recv: CSObj, callee: Method): IContext {
        val context = when(recv.context.depth) {
            0 -> {
                Context<Obj>(k)
            }
            else -> {
                Context<Obj>(recv.context)
            }
        }
        context.addElement(recv.obj)
        return context
    }

    override fun selectHeapContext(method: CSMethod, allocationSite: Any): IContext {
        val context = method.context
        return if (context.depth > 1) {
            val newContext = Context<Obj>(context.depth - 1)
            newContext.addElement(context)
            newContext
        } else {
            context
        }
    }
}