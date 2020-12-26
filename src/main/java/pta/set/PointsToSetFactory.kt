package pta.set

import pta.analysis.data.CSObj

interface PointsToSetFactory {
    fun makePointsToSet(): PointsToSet

    fun makePointsToSet(obj: CSObj): PointsToSet {
        val set = makePointsToSet()
        set.addObject(obj)
        return set
    }
}