package pta.analysis.data

import pta.analysis.context.ObjectSensitive
import pta.set.PointsToSet

data class ArrayField constructor(
    val array: ObjectSensitive
): Pointer {
    override var pointsToSet: PointsToSet? = null

    override fun toString(): String {
        return "$array[*]"
    }
}