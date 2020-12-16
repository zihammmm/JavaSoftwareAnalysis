package pta.analysis.heap

import pta.element.Method
import pta.element.Obj
import pta.element.Type

class AllocationSiteBasedModel: HeapModel {
    private val objects = HashMap<Any, Obj>()

    override fun getObj(allocationSite: Any, type: Type, containerMethod: Method): Obj {
        return objects.computeIfAbsent(allocationSite) {
            ObjImpl(allocationSite, type, containerMethod)
        }
    }
}