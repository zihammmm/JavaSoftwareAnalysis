package pta.analysis.data

import pta.analysis.context.IContext
import pta.element.Method

data class CSMethod constructor(
    val method: Method,
    val iContext: IContext
): AbstractCSElement(iContext) {

    override fun toString(): String {
        return "$method:$context"
    }
}