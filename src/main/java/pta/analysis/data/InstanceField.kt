package pta.analysis.data

import pta.element.Field
import pta.set.PointsToSet

data class InstanceField constructor(
    val base: CSObj,
    val field: Field
):Pointer {
    override var pointsToSet: PointsToSet? = null

    override fun toString(): String {
        return "$base.$field"
    }
}