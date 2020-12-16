package pta.element

import pta.statement.Call
import pta.statement.InstanceLoad
import pta.statement.InstanceStore

interface Variable {
    fun getType(): Type

    fun getContainerMethod(): Method

    fun getName(): String

    fun getCalls(): MutableSet<Call>

    fun getLoads(): MutableSet<InstanceLoad>

    fun getStores(): MutableSet<InstanceStore>
}