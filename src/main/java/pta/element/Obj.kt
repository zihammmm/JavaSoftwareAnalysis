package pta.element

interface Obj {
    fun getType(): Type

    fun getAllocationSite(): Any

    fun getContainerMethod(): Method

    enum class Kind{
        NORMAL,
        STRING_CONSTANT
    }
}