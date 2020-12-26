package pta.analysis.data

import pta.set.PointsToSet

interface Pointer {
    var pointsToSet: PointsToSet?

}