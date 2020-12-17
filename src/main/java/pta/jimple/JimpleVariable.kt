package pta.jimple

import pta.element.Method
import pta.element.Type
import pta.element.Variable
import pta.statement.Call
import pta.statement.InstanceLoad
import pta.statement.InstanceStore
import soot.Local


data class JimpleVariable(
    val variable: Local,
    val type: JimpleType,
    val containerMethod: JimpleMethod
): Variable {
    companion object {
        private const val initialCapacityOfSet = 4
    }
    private var calls = mutableSetOf<Call>()
    private var stores = mutableSetOf<InstanceStore>()
    private var loads = mutableSetOf<InstanceLoad>()

    fun addCall(call: Call) {
        if (calls.isEmpty()) {
            calls = LinkedHashSet(initialCapacityOfSet)
        }
        calls.add(call)
    }

    fun addStore(store: InstanceStore) {
        if (stores.isEmpty()) {
            stores = LinkedHashSet(initialCapacityOfSet)
        }
        stores.add(store)
    }

    fun addLoad(load: InstanceLoad) {
        if (loads.isEmpty()) {
            loads = LinkedHashSet(initialCapacityOfSet)
        }
        loads.add(load)
    }

    override fun getType(): Type {
        return type
    }

    override fun getContainerMethod(): Method {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        TODO("Not yet implemented")
    }

    override fun getCalls(): MutableSet<Call> {
        TODO("Not yet implemented")
    }

    override fun getLoads(): MutableSet<InstanceLoad> {
        TODO("Not yet implemented")
    }

    override fun getStores(): MutableSet<InstanceStore> {
        TODO("Not yet implemented")
    }

}
