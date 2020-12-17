package pta.element

interface Field {
    val isInstance: Boolean

    val isStatic: Boolean

    val classType: Type

    val name: String

    val fieldType: Type

}