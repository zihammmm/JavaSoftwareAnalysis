package pta.analysis.solver

abstract class AbstractPointer {
    private val pointsToSet = PointsToSet()

    fun getPointsToSet() = pointsToSet

}