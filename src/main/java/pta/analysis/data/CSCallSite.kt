package pta.analysis.data

import pta.analysis.context.IContext
import pta.element.CallSite

data class CSCallSite constructor(
    val callSite: CallSite,
    val context: IContext
): AbstractCSElement(context) {

    override fun toString(): String {
        return "$context:$callSite"
    }
}