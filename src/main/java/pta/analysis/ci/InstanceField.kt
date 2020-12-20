package pta.analysis.ci

import pta.element.Field
import pta.element.Obj

/**
 * Represents instance field nodes in the PFG
 */
class InstanceField constructor(
    private val base: Obj,
    private val field: Field
): AbstractPointer() {

    fun getBase() = base

    fun getField() = field

    override fun toString(): String {
        return "$base.$field"
    }
}