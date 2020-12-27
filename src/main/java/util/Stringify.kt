package util

fun <T> Sequence<T>.sequenceToString(): String {
    val elements = Iterable {
        this.map {
            it.toString()
        }.sorted().iterator()
    }
    return "{${elements.joinToString()}}"
}