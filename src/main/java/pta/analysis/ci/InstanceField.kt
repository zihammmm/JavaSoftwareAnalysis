package pta.analysis.ci

import pta.element.Field
import pta.element.Obj

class InstanceField constructor(
    private val base: Obj,
    private val field: Field
): Pointer() {

    fun getBase() = base

    fun getField() = field

    override fun toString(): String {
        return "$base.$field"
    }
}