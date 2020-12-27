package pta.analysis.heap

import pta.element.Method
import pta.element.Obj
import pta.element.Type

data class ObjImpl constructor(
    private val allocation: Any,
    private val type: Type,
    private val containerMethod: Method
): Obj {

    override fun getType() = type

    override fun getAllocationSite() = allocation

    override fun getContainerMethod(): Method = containerMethod

    override fun equals(other: Any?): Boolean {
        return if (this === other) {
            true
        } else if (other != null && other is ObjImpl) {
            allocation == other.allocation
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return allocation.hashCode()
    }

    override fun toString(): String {
        return allocation.toString()
    }
}