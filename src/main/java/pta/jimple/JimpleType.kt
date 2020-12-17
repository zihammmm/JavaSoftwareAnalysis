package pta.jimple

import pta.element.Type
import soot.RefType
import soot.SootClass

data class JimpleType(
    val sootType: soot.Type
) : Type {
    val sootClass: SootClass? =
        if (sootType is RefType) {
            sootType.sootClass
        } else {
            null
        }

    override val name: String = sootType.toString()

    override fun equals(other: Any?): Boolean {
        return if (this === other) {
            true
        } else if (other != null && this::class == other::class) {
            return sootType == (other as JimpleType).sootType
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return sootType.hashCode()
    }

    override fun toString(): String {
        return sootType.toString()
    }

}
