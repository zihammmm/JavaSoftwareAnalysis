package util

data class MutableInt(
    var value: Int
) {
    fun increase() = value.inc()

    override fun equals(other: Any?): Boolean {
        return if (this === other) {
            true
        } else if (other != null && this::class == other::class) {
            return value == (other as MutableInt).value
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return value
    }

    override fun toString(): String {
        return value.toString()
    }
}