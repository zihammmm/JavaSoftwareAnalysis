package pta.element

interface Field {
    val isInstance: Boolean

    val isStatic: Boolean

    fun getClassType(): Type

    fun getName(): String?

    fun getFieldType(): Type

}