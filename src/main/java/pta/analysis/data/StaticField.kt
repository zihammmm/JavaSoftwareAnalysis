package pta.analysis.data

import pta.element.Field
import pta.set.PointsToSet

data class StaticField constructor(
    val field: Field
):Pointer {
    override var pointsToSet: PointsToSet? = null

    override fun toString(): String {
        return field.toString()
    }
}