package pta.set

import pta.analysis.context.ObjectSensitive
import pta.analysis.data.CSObj
import util.HybridArrayHashSet

class HybridPointsToSet: DelegatePointsToSet() {
    override var set: MutableSet<CSObj> = HybridArrayHashSet()

    class Factory: PointsToSetFactory{
        override fun makePointsToSet(): PointsToSet {
            return HybridPointsToSet()
        }

    }

}