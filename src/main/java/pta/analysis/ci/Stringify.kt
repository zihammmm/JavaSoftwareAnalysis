package pta.analysis.ci

import pta.element.Obj
import soot.jimple.AssignStmt

fun AbstractPointer.pointerToString(): String {
    return if (this is InstanceField) {
        "${getBase().objToString()}.${getField().getName()}"
    } else {
        this.toString()
    }
}

fun Obj.objToString(): String {
    val sb = StringBuilder()
    getContainerMethod()?.let {
        sb.append(it).append('/')
    }

    val allocation = getAllocationSite()
    if (allocation is AssignStmt) {
        sb.append(allocation.rightOp).append('/').append(allocation.javaSourceStartLineNumber)
    } else {
        sb.append(allocation)
    }

    return sb.toString()
}

fun PointsToSet.pointsToSetToString(): String {
    val objs = Iterable{asSequence()
        .map {
            it.objToString()
        }
        .sorted()
        .iterator()
    }

    return "{${objs.joinToString(",")}}"
}