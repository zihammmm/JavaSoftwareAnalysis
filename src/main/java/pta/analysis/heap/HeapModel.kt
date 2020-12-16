package pta.analysis.heap

import pta.element.Method
import pta.element.Obj
import pta.element.Type

interface HeapModel {
    fun getObj(allocationSite: Any, type: Type, containerMethod: Method): Obj
}