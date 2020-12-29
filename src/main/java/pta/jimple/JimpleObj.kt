package pta.jimple

import pta.element.Method
import pta.element.Obj
import pta.element.Type
import soot.jimple.AssignStmt

data class JimpleObj(
    val allocation: Any,
    val type: JimpleType,
    val containerMethod: JimpleMethod
): Obj{
    override fun getType(): Type {
        return type
    }

    override fun getAllocationSite(): Any {
        return allocation
    }

    override fun getContainerMethod(): Method {
        return containerMethod
    }

    override fun equals(other: Any?): Boolean {
        return if (this === other) {
            true
        } else if (other != null && other is JimpleObj) {
            return allocation == other.allocation
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return allocation.hashCode()
    }

    override fun toString(): String {
        val sb = StringBuilder()

        containerMethod.let {
            sb.append(it).append("/")
        }
        if (allocation is AssignStmt) {
            sb.append(allocation.rightOp).append("/").append(allocation.javaSourceStartLineNumber)
        } else {
            sb.append(allocation)
        }

        return sb.toString()
    }
}
