package pta.set

import pta.analysis.data.CSObj

interface PointsToSet: Iterable<CSObj> {
    fun addObject(obj: CSObj): Boolean

    fun isEmpty(): Boolean

    fun sequence(): Sequence<CSObj>
}