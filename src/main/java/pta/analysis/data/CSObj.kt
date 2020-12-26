package pta.analysis.data

import pta.analysis.context.IContext
import pta.element.Obj

data class CSObj constructor(
    val obj: Obj,
    val iContext: IContext
): AbstractCSElement(iContext) {
    override fun toString(): String {
        return "$context:$obj"
    }
}