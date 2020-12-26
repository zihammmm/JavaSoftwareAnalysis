package pta.analysis.data

import pta.analysis.context.IContext
import pta.element.Variable
import pta.set.PointsToSet

data class CSVariable constructor(
    val variable: Variable,
    val iContext: IContext
): Pointer, AbstractCSElement(iContext) {

    override var pointsToSet: PointsToSet? = null

    override fun toString(): String {
        return "$context:$variable"
    }

}