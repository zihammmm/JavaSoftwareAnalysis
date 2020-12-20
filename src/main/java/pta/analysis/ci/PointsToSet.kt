package pta.analysis.ci

import pta.element.Obj
import util.HybridArrayHashSet

class PointsToSet: Iterable<Obj> {
    private val set = HybridArrayHashSet<Obj>()

    constructor()
    constructor(obj: Obj) {
        addObject(obj)
    }

    fun addObject(obj: Obj) = set.add(obj)


    val isEmpty = set.isEmpty()

    fun sequence() = set.asSequence()


    override fun iterator(): Iterator<Obj> {
        return set.iterator()
    }

    override fun toString(): String {
        return set.toString()
    }
}