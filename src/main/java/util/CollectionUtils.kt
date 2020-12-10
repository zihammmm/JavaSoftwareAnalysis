package util


fun <K, E> HashMap<K, MutableSet<E>>.addToMapSet(key: K, element: E): Boolean {
    return computeIfAbsent(key, { newSet()})
        .add(element)
}

fun <K1, K2, V> MutableMap<K1, MutableMap<K2, V>>.addToMapMap(key1: K1, key2: K2, value: V) {
    computeIfAbsent(key1, { newMap()})
        .put(key2, value)
}

fun <E> newSet() = HashSet<E>()

fun <K, V> newMap() = HashMap<K, V>()

fun <K1, K2, V> Map<K1, Map<K2, V>>.getAllValues(): Sequence<V> {
    return values.asSequence().flatMap {m ->
        m.entries.asIterable()
    }.map {
        it.value
    }
}