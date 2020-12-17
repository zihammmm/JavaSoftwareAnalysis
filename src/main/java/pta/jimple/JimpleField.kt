package pta.jimple

import pta.element.Field
import soot.SootField

data class JimpleField(
    val field: SootField,
    override val classType: JimpleType,
    override val fieldType: JimpleType
): Field {
    override val isStatic: Boolean = field.isStatic

    override val isInstance: Boolean = !field.isStatic

    override val name: String = field.name

    override fun equals(other: Any?): Boolean {
        return if (this === other) {
            true
        } else if (other != null && this::class == other::class) {
            return field == (other as JimpleField).field
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return field.hashCode()
    }

    override fun toString(): String {
        return field.toString()
    }

}