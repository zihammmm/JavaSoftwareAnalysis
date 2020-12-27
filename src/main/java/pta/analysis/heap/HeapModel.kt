package pta.analysis.heap

import pta.element.Obj
import pta.statement.Allocation

interface HeapModel {
    fun getObj(allocationSite: Allocation): Obj
}