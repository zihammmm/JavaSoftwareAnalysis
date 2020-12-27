package pta.analysis.data

import pta.analysis.context.IContext
import pta.element.Obj

data class CSObj constructor(
    val obj: Obj,
    val context: IContext
): AbstractCSElement(context) {
    override fun toString(): String {
        return "$context:$obj"
    }
}