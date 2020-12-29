package pta.analysis.selector

import pta.analysis.context.Context
import pta.analysis.context.IContext
import pta.analysis.data.CSCallSite
import pta.analysis.data.CSMethod
import pta.analysis.data.CSObj
import pta.element.Method
import pta.element.Obj
import pta.element.Type

/**
 * k-type-sensitive
 */
class TypeSelector constructor(
    private val k: Int
) : ContextSelector {
    override fun selectContext(callSite: CSCallSite, callee: Method): IContext {
        return callSite.context
    }

    override fun selectContext(callSite: CSCallSite, recv: CSObj, callee: Method): IContext {
        val context = when (recv.context.depth) {
            0 -> {
                Context<Type>(k)
            }
            else -> {
                Context<Type>(recv.context)
            }
        }
        val type = recv.obj.getContainerMethod().getClassType()
        mergeType(context, type)
        return context
    }

    override fun selectHeapContext(method: CSMethod, allocationSite: Any): IContext {
        val context = method.context
        return if (context.depth > 1) {
            val newContext = Context<Type>(context.depth - 1)
            newContext.addElement(context)
            newContext
        } else {
            context
        }
    }

    private fun mergeType(context: Context<Type>, type: Type): Context<Type> {
        return if (context.containsElement(type)) {
            context
        } else {
            context.addElement(type)
            context
        }
    }
}