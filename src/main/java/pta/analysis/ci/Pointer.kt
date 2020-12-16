package pta.analysis.ci

abstract class Pointer {
    private val pointsToSet = PointsToSet()

    fun getPointsToSet() = pointsToSet

}