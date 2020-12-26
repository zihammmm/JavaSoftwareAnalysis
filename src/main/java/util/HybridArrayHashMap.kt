package util

import java.io.Serializable

class HybridArrayHashMap<K, V> : MutableMap<K, V>, Serializable {
    companion object {
        private const val NULL_KEY = "HybridArrayHashMap"
        private const val ARRAY_SIZE = 8

        private fun <K, V> getFirstEntry(m: Map<K, V>): Map.Entry<K, V>? {
            return if (m !is HybridArrayHashMap) {
                m.entries.iterator().next()
            } else {
                if (m.singletonKey != null && m.singletonValue != null) {
                    MapEntry(m.singletonKey!!, m.singletonValue!!)
                } else if (m.arrayKeys.isNotEmpty()) {
                    MapEntry(m.arrayKeys[0] as K, m.arrayValues[0] as V)
                } else {
                    m.hashMap?.entries?.iterator()?.next()
                }
            }
        }

        private class EmptyIterator<E> : MutableIterator<E> {
            override fun hasNext(): Boolean {
                return false
            }

            override fun next(): E {
                throw NoSuchElementException()
            }

            override fun remove() {
                throw IllegalStateException()
            }

        }
    }

    private var singletonKey: K? = null
    private var singletonValue: V? = null
    private var arrayKeys = emptyArray<Any?>()
    private var arrayValues = emptyArray<Any?>()
    private var numberOfUsedArrayEntries = 0
    private var hashMap: MutableMap<K, V>? = null

    constructor()
    constructor(m: Map<K, V>) {
        val size = m.size
        when {
            size == 1 -> {
                val e = getFirstEntry(m)
                singletonKey = e?.key
                singletonValue = e?.value
            }
            size <= 8 -> {
                arrayKeys = arrayOfNulls(ARRAY_SIZE)
                arrayValues = arrayOfNulls(ARRAY_SIZE)
                for (entry in m.entries) {
                    arrayKeys[numberOfUsedArrayEntries] = entry.key
                    arrayValues[numberOfUsedArrayEntries++] = entry.value
                }
            }
            else -> {
                hashMap = HashMap(size + 8)
                hashMap?.putAll(m)
            }
        }
    }

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = hashMap?.entries ?: object : MutableSet<MutableMap.MutableEntry<K, V>> {
            override fun add(element: MutableMap.MutableEntry<K, V>): Boolean {
                TODO("Not yet implemented")
            }

            override fun addAll(elements: Collection<MutableMap.MutableEntry<K, V>>): Boolean {
                TODO("Not yet implemented")
            }

            override fun clear() {
                this@HybridArrayHashMap.clear()
            }

            override fun iterator(): MutableIterator<MutableMap.MutableEntry<K, V>> {
                if (singletonKey != null) {
                    return object : HybridArrayHashMap<K, V>.SingletonIterator<MutableMap.MutableEntry<K, V>>() {
                        override fun next(): MutableMap.MutableEntry<K, V> {
                            return nextEntry()
                        }
                    }
                } else {
                    if (arrayKeys.isNotEmpty()) {
                        return object : HybridArrayHashMap<K, V>.ArrayIterator<MutableMap.MutableEntry<K, V>>() {
                            override fun next(): MutableMap.MutableEntry<K, V> {
                                return nextEntry()
                            }
                        }
                    } else {
                        return EmptyIterator()
                    }
                }
            }

            override fun remove(element: MutableMap.MutableEntry<K, V>): Boolean {
                TODO("Not yet implemented")
            }

            override fun removeAll(elements: Collection<MutableMap.MutableEntry<K, V>>): Boolean {
                TODO("Not yet implemented")
            }

            override fun retainAll(elements: Collection<MutableMap.MutableEntry<K, V>>): Boolean {
                TODO("Not yet implemented")
            }

            override val size: Int
                get() = this@HybridArrayHashMap.size

            override fun contains(element: MutableMap.MutableEntry<K, V>): Boolean {
                if (singletonKey != null) {
                    return singletonKey == element.key && singletonValue == element.value
                } else {
                    if (arrayKeys.isNotEmpty()) {
                        for (i in 0 until ARRAY_SIZE) {
                            if (arrayKeys[i] != null && arrayKeys[i] == element.key && arrayValues[i] == element.value) {
                                return true
                            }
                        }
                    }
                    return false
                }
            }

            override fun containsAll(elements: Collection<MutableMap.MutableEntry<K, V>>): Boolean {
                for (element in elements) {
                    if (!contains(element)) {
                        return false
                    }
                }
                return true
            }

            override fun isEmpty(): Boolean {
                return singletonKey == null && arrayKeys.isEmpty() && hashMap?.isEmpty() ?: false
            }
        }

    override val keys: MutableSet<K>
        get() = hashMap?.keys ?: mutableSetOf()

    override val size: Int
        get() = when {
            singletonKey != null -> {
                1
            }
            arrayKeys.isNotEmpty() -> {
                numberOfUsedArrayEntries
            }
            else -> {
                hashMap?.size ?: 0
            }
        }

    override val values: MutableCollection<V>
        get() = hashMap?.values ?: object : MutableCollection<V> {
            override val size: Int
                get() = this@HybridArrayHashMap.size

            override fun contains(element: V): Boolean {
                if (singletonKey != null) {
                    return singletonValue?.equals(element) ?: false
                } else {
                    if (arrayKeys.isNotEmpty()) {
                        for (i in 0 until ARRAY_SIZE) {
                            if (arrayKeys[i] != null && arrayValues[i] == element) {
                                return true
                            }
                        }
                    }
                    return false
                }
            }

            override fun containsAll(elements: Collection<V>): Boolean {
                for (element in elements) {
                    if (!contains(element)) {
                        return false
                    }
                }
                return true
            }

            override fun isEmpty(): Boolean {
                return singletonKey == null || arrayKeys.isEmpty()
            }

            override fun add(element: V): Boolean {
                return false
            }

            override fun addAll(elements: Collection<V>): Boolean {
                var change = false
                for (element in elements) {
                    change = change || add(element)
                }
                return change
            }

            override fun clear() {
                this@HybridArrayHashMap.clear()
            }

            override fun iterator(): MutableIterator<V> {
                if (singletonKey != null) {
                    return object : HybridArrayHashMap<K, V>.SingletonIterator<V>() {
                        override fun next(): V {
                            return nextValue()
                        }
                    }
                } else {
                    return if (arrayKeys.isNotEmpty()) {
                        object : HybridArrayHashMap<K, V>.SingletonIterator<V>() {
                            override fun next(): V {
                                return nextValue()
                            }
                        }
                    } else {
                        EmptyIterator()
                    }
                }
            }

            override fun remove(element: V): Boolean {
                throw IllegalStateException()
            }

            override fun removeAll(elements: Collection<V>): Boolean {
                var changed = false
                for (element in elements) {
                    changed = changed || remove(element)
                }
                return changed
            }

            override fun retainAll(elements: Collection<V>): Boolean {
                throw IllegalStateException()
            }

        }


    override fun containsKey(key: K): Boolean {
        when {
            singletonKey != null -> return singletonKey == key
            arrayKeys.isNotEmpty() -> {
                for (i in 0 until 8) {
                    if (arrayKeys[i] != null && arrayKeys[i] == key) {
                        return true
                    }
                }
                return false
            }
            else -> return hashMap?.containsKey(key) ?: false
        }
    }

    override fun containsValue(value: V): Boolean {
        return when {
            singletonKey != null -> singletonValue == value
            arrayKeys.isNotEmpty() -> {
                for (i in 0 until 8) {
                    if (arrayKeys[i] != null && arrayValues[i] == value) {
                        return true
                    }
                }
                false
            }
            else -> hashMap?.containsValue(value) ?: false
        }
    }

    override fun get(key: K): V? {
        return when {
            singletonKey != null -> if (singletonKey == key) {
                singletonValue
            } else {
                null
            }
            arrayKeys.isNotEmpty() -> {
                for (i in 0 until ARRAY_SIZE) {
                    if (arrayKeys[i] != null && arrayKeys[i] == key) {
                        arrayValues[i]
                    }
                }
                null
            }
            else -> hashMap?.get(key)
        }
    }

    override fun isEmpty(): Boolean {
        return singletonKey == null && arrayKeys.isEmpty() && hashMap?.isEmpty() ?: true
    }

    override fun clear() {
        if (singletonKey != null) {
            singletonKey = null
            singletonValue = null
        } else if (arrayKeys.isNotEmpty()) {
            arrayKeys = arrayOfNulls(ARRAY_SIZE)
            arrayValues = arrayOfNulls(ARRAY_SIZE)
            numberOfUsedArrayEntries = 0
        } else {
            hashMap?.clear()
            hashMap = null
        }
    }

    override fun put(key: K, value: V): V? {
        if (singletonKey != null) {
            if (singletonKey == key) {
                val old = singletonValue as V
                singletonValue = value
                return old
            }
            convertSingletonToArray()
        }
        if (arrayKeys.isNotEmpty()) {
            for (i in 0 until 8) {
                if (arrayKeys[i] != null && arrayKeys[i] == key) {
                    val old = arrayValues[i] as V
                    arrayValues[i] = value
                    return old
                }
            }
            for (i in 0 until 8) {
                if (arrayKeys[i] == null) {
                    arrayKeys[i] = key
                    arrayValues[i] = value
                    numberOfUsedArrayEntries++
                    return null
                }
            }

            convertArrayToHashMap()
        }
        return if (hashMap != null) {
            hashMap?.put(key, value)
        } else {
            singletonKey = key
            singletonValue = value
            null
        }
    }

    override fun putAll(from: Map<out K, V>) {
        val size = from.size
        if (size != 0) {
            val newSize = size + this.size
            if (arrayKeys.isEmpty() && hashMap == null && newSize == 1) {
                val e = getFirstEntry(from)
                if (e?.key == null) {
                    throw NullPointerException("HybridArrayHashMap does not permit null keys ")
                } else {
                    singletonKey = e.key
                    singletonValue = e.value
                }
            } else {
                if (arrayKeys.isEmpty() && hashMap == null && newSize <= 8) {
                    if (singletonKey != null) {
                        convertSingletonToArray()
                    } else {
                        arrayKeys = arrayOfNulls(ARRAY_SIZE)
                        arrayValues = arrayOfNulls(ARRAY_SIZE)
                        numberOfUsedArrayEntries = 0
                    }
                }

                if (arrayKeys.isEmpty() && newSize <= 8) {
                    var next = 0
                    for (entry in from.entries) {
                        if (entry.key == null) {
                            throw NullPointerException("HybridArrayHashMap does not permit null keys")
                        }
                        for (i in 0 until 8) {
                            if (arrayKeys[i] != null && arrayKeys[i] == entry.key) {
                                arrayValues[i] = entry.value
                                continue
                            }
                        }
                        while (arrayKeys[next] != null) {
                            ++next
                        }
                        arrayKeys[next] = entry.key
                        arrayValues[next] = entry.value
                        numberOfUsedArrayEntries++
                    }
                } else {
                    if (arrayKeys.isEmpty()) {
                        convertArrayToHashMap()
                    }
                    if (hashMap == null) {
                        hashMap = HashMap(ARRAY_SIZE + newSize)
                    }
                    if (singletonKey != null) {
                        hashMap!![singletonKey!!] = singletonValue!!
                        singletonKey = null
                        singletonValue = null
                    }
                    hashMap!!.putAll(from)
                }
            }
        }
    }

    override fun remove(key: K): V? {
        if (singletonKey != null) {
            if (singletonKey == key) {
                val old = singletonValue
                singletonKey = null
                singletonValue = null
                return old
            } else {
                return null
            }
        } else if (arrayKeys.isNotEmpty()) {
            for (i in 0 until 8) {
                if (arrayKeys[i] != null && arrayKeys[i] == key) {
                    val v = arrayValues[i]
                    arrayKeys[i] = null
                    arrayValues[i] = null
                    numberOfUsedArrayEntries--
                    return v as V
                }
            }
            return null
        } else {
            return hashMap?.remove(key)
        }
    }

    private fun convertSingletonToArray() {
        arrayKeys = arrayOfNulls(ARRAY_SIZE)
        arrayValues = arrayOfNulls(ARRAY_SIZE)
        arrayKeys[0] = singletonKey
        arrayValues[0] = singletonValue
        numberOfUsedArrayEntries = 1
        singletonKey = null
        singletonValue = null
    }

    private fun convertArrayToHashMap() {
        hashMap = HashMap(ARRAY_SIZE shl 1)
        for (i in 0 until 8) {
            if (arrayKeys[i] != null) {
                hashMap!![arrayKeys[i]!! as K] = arrayValues[i]!! as V
            }
        }
        arrayKeys = emptyArray()
        arrayValues = emptyArray()
        numberOfUsedArrayEntries = 0
    }

    private abstract inner class SingletonIterator<E> : MutableIterator<E> {
        var done = true

        override fun hasNext(): Boolean {
            return !done
        }

        fun nextEntry(): MutableMap.MutableEntry<K, V> {
            if (done) {
                throw NoSuchElementException()
            } else {
                val e = MapEntry(singletonKey!!, singletonValue!!)
                done = true
                return e as MutableMap.MutableEntry<K, V>
            }
        }

        fun nextKey(): K {
            if (done) {
                throw NoSuchElementException()
            } else {
                done = true
                return singletonKey!!
            }
        }

        fun nextValue(): V {
            if (done) {
                throw NoSuchElementException()
            } else {
                done = true
                return singletonValue!!
            }
        }

        override fun remove() {
            if (done && singletonKey != null) {
                singletonKey = null
                singletonValue = null
            } else {
                throw IllegalStateException()
            }
        }
    }

    private abstract inner class ArrayIterator<E> : MutableIterator<E> {
        var next = 0
        var last = -1

        init {
            findNext()
        }

        private fun findNext() {
            while (next < ARRAY_SIZE && arrayKeys[next] == null) {
                ++next
            }
        }

        override fun hasNext() = next < ARRAY_SIZE

        fun nextEntry(): MutableMap.MutableEntry<K, V> {
            if (next == ARRAY_SIZE) {
                throw NoSuchElementException()
            } else {
                last = next
                val e = MapEntry(arrayKeys[next] as K, arrayValues[next] as V)
                findNext()
                return e
            }
        }

        fun nextKey(): K {
            if (next == ARRAY_SIZE) {
                throw NoSuchElementException()
            } else {
                last = next
                val k = arrayKeys[next++]
                findNext()
                return k as K
            }
        }

        fun nextValue(): V {
            if (next == ARRAY_SIZE) {
                throw NoSuchElementException()
            } else {
                last = next
                val v = arrayValues[next++]
                findNext()
                return v as V
            }
        }

        override fun remove() {
            if (last != -1 && arrayKeys[last] != null) {
                arrayKeys[last] = null
                arrayValues[last] = null
                numberOfUsedArrayEntries--
                findNext()
            } else {
                throw IllegalStateException()
            }
        }
    }
}