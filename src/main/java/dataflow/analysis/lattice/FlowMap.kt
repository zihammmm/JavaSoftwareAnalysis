package dataflow.analysis.lattice

interface FlowMap<K, V>: Map<K, V> {
    fun update(k: K, v: V): Boolean

    fun copyFrom(map: FlowMap<K, V>?): Boolean {
        var changed = false
        map?.let {
            for (entry in it.entries) {
                changed = changed or update(entry.key, entry.value)
            }
        }
        return changed
    }
}