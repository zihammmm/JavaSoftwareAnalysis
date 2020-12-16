package pta.analysis.ci

import pta.element.Obj
import util.HybridArrayHashSet

class PointsToSet(
    obj: Obj
): Iterable<Obj> {
    private val set = HybridArrayHashSet<Obj>()

    constructor()

    fun addObject(obj: Obj) = set.add(obj)

    init {
        addObject(obj)
    }

    val isEmpty = set.isEmpty()

    fun sequence() = set.asSequence()


    override fun iterator(): Iterator<Obj> {
        return set.iterator()
    }

    override fun toString(): String {
        return set.toString()
    }
}