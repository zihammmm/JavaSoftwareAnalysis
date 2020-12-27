package pta.analysis.data

import pta.analysis.context.IContext
import pta.element.Variable
import pta.set.PointsToSet

data class CSVariable constructor(
    val variable: Variable,
    val context: IContext
): Pointer, AbstractCSElement(context) {

    override var pointsToSet: PointsToSet? = null

    override fun toString(): String {
        return "$context:$variable"
    }

}