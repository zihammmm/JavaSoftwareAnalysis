package pta.analysis.data

import pta.analysis.context.IContext
import pta.element.CallSite

data class CSCallsite constructor(
    val callSite: CallSite,
    val iContext: IContext
): AbstractCSElement(iContext) {

    override fun toString(): String {
        return "$context:$callSite"
    }
}