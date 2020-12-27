package pta.analysis.data

import pta.analysis.context.IContext
import pta.element.Method

data class CSMethod constructor(
    val method: Method,
    val context: IContext
): AbstractCSElement(context) {

    override fun toString(): String {
        return "$method:$context"
    }
}