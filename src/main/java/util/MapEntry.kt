package util

import java.io.Serializable

data class MapEntry<K, V> constructor(
    override val key: K,
    override var value: V
): MutableMap.MutableEntry<K, V>, Serializable {

    override fun equals(other: Any?): Boolean {
        return if (other === this) {
            true
        } else if (other !is Map.Entry<*, *>) {
            false
        } else {
            key == other.key && value == other.value
        }
    }

    override fun hashCode(): Int {
        return key.hashCode() xor value.hashCode()
    }

    override fun setValue(newValue: V): V {
        val old = value
        value = newValue
        return old
    }
}