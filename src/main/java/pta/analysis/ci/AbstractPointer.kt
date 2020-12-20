package pta.analysis.ci

abstract class AbstractPointer {
    private val pointsToSet = PointsToSet()

    fun getPointsToSet() = pointsToSet

}