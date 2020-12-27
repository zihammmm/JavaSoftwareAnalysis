package pta.analysis.heap

import pta.element.Obj
import pta.statement.Allocation

class AllocationSiteBasedModel: HeapModel {
    private val objects = HashMap<Allocation, Obj>()

    override fun getObj(allocationSite: Allocation): Obj {
        return objects.computeIfAbsent(allocationSite) {
            it.obj
        }
    }
}