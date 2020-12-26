package pta.set

import pta.analysis.data.CSObj

open class DelegatePointsToSet: PointsToSet {
    protected open var set = mutableSetOf<CSObj>()

    override fun addObject(obj: CSObj): Boolean {
        return set.add(obj)
    }

    override fun isEmpty(): Boolean {
        return set.isEmpty()
    }

    override fun sequence(): Sequence<CSObj> {
        return set.asSequence()
    }

    override fun iterator(): Iterator<CSObj> {
        return set.iterator()
    }

}