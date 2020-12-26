package util

import java.io.Serializable

class HybridArrayHashSet<V> : MutableSet<V>, Serializable {
    companion object {
        private val NULL_KEY = "HybridArrayHashSet does not permit null keys"
        private val ARRAY_SIZE = 16

        private fun <V> getFirstElement(m: Collection<V>): V? {
            if (m !is HybridArrayHashSet) {
                return m.iterator().next()
            }
            m.singleton?.let {
                return it
            }
            m.array?.let {
                for (i in 0 until ARRAY_SIZE) {
                    it[i]?.let { element ->
                        return element
                    }
                }
                return null
            }
            m.hashSet?.let {
                return it.iterator().next()
            }
            return null
        }
    }

    private var singleton: V? = null
    private var numberOfUsedArrayEntries = 0
    private var array: Array<V?>? = null
    private var hashSet: HashSet<V>? = null

    constructor()
    constructor(m: Collection<V>) {
        when {
            m.size == 1 -> singleton = getFirstElement(m)
            m.size <= ARRAY_SIZE -> {
                @Suppress("unchecked")
                array = arrayOfNulls<Any>(ARRAY_SIZE) as Array<V?>
                numberOfUsedArrayEntries = 0
                val isSet = m is MutableSet
                for (v in m) {
                    if (v == null) {
                        throw NullPointerException(NULL_KEY)
                    }
                    if (!isSet || !array!!.contains(v)) {
                        //不是set或者还没有加进array中
                        array!![numberOfUsedArrayEntries++] = v
                    }
                }
            }
            m.isEmpty() -> {
            }
            else -> {
                for (v in m) {
                    if (v == null) {
                        throw NullPointerException(NULL_KEY)
                    }
                }
                hashSet = HashSet(m)
            }
        }
    }

    override fun add(element: V): Boolean {
        singleton?.let {
            if (it == element) {
                return false
            }
            convertArrayToHashSet()
        }
        array?.let {
            for (i in 0 until ARRAY_SIZE) {
                if (it[i] != null && it[i] != element) {
                    return false
                }
            }
            for (i in 0 until ARRAY_SIZE) {
                if (it[i] == null) {
                    it[i] = element
                    numberOfUsedArrayEntries++
                    return true
                }
            }
            convertArrayToHashSet()
        }
        hashSet?.let {
            return it.add(element)
        }
        singleton = element
        return true
    }

    @Suppress("unchecked")
    private fun convertSingletonToArray() {
        array = arrayOfNulls<Any>(ARRAY_SIZE) as Array<V?>
        array!![0] = singleton!!
        numberOfUsedArrayEntries = 1
        singleton = null
    }

    private fun convertArrayToHashSet() {
        hashSet = HashSet(ARRAY_SIZE shl 1)
        for (i in 0 until ARRAY_SIZE) {
            array!![i]?.let {
                hashSet!!.add(it)
            }
        }
        array = null
        numberOfUsedArrayEntries = 0
    }

    fun toArray(): Array<Any> {
        singleton?.let {
            return arrayOf(it)
        }
        array?.let {
            return it.asSequence()
                .filter { element ->
                    element != null
                }
                .toCollection(ArrayList())
                .toArray()
        }
        hashSet?.let {
            return it.toArray()
        }
        return emptyArray()
    }

    override fun addAll(elements: Collection<V>): Boolean {
        if (elements.isEmpty()) {
            return false
        }
        val maxNewSize = elements.size + size
        if (array == null && hashSet == null) {
            if (maxNewSize == 1) {
                val v = getFirstElement(elements) ?: throw NullPointerException(NULL_KEY)
                singleton = v
                return true
            } else if (maxNewSize <= ARRAY_SIZE) {
                if (singleton != null) {
                    convertSingletonToArray()
                } else {
                    array = arrayOfNulls<Any>(ARRAY_SIZE) as Array<V?>
                    numberOfUsedArrayEntries = 0
                }
            }
        }
        if (array != null && maxNewSize <= ARRAY_SIZE) {
            var changed = false
            var next = 0
            loop@ for (e in elements) {
                if (e == null) throw NullPointerException(NULL_KEY)
                for (i in 0 until ARRAY_SIZE) if (array!![i] != null && array!![i] == e) continue@loop
                while (array!![next] != null) next++
                array!![next++] = e
                numberOfUsedArrayEntries++
                changed = true
            }
            return changed
        }
        for (v in elements) {
            if (v == null) {
                throw NullPointerException(NULL_KEY)
            }
        }
        array?.let {
            convertArrayToHashSet()
        }
        if (hashSet == null) {
            hashSet = HashSet(ARRAY_SIZE + maxNewSize)
        }
        singleton?.let {
            hashSet!!.add(it)
            singleton = null
        }
        return hashSet!!.addAll(elements)
    }

    override fun clear() {
        if (singleton != null) {
            singleton = null
        } else if (array != null) {
            array?.fill(null)
            numberOfUsedArrayEntries = 0
        } else if (hashSet != null) {
            hashSet?.clear()
        }
    }

    override fun contains(element: V): Boolean {
        singleton?.let {
            return it == element
        }
        array?.let {
            for (i in 0 until ARRAY_SIZE) {
                if (it[i] != null && it[i] == element) {
                    return true
                }
            }
            return false
        }
        hashSet?.let {
            return it.contains(element)
        }
        return false
    }

    override fun containsAll(elements: Collection<V>): Boolean {
        for (o in elements) {
            if (!contains(o)) {
                return false
            }
        }
        return true
    }

    override fun isEmpty(): Boolean {
        return when {
            singleton != null -> {
                false
            }
            array != null -> {
                numberOfUsedArrayEntries == 0
            }
            hashSet != null -> {
                hashSet!!.isEmpty()
            }
            else -> {
                true
            }
        }
    }

    override val size: Int
        get() = when {
            singleton != null -> {
                1
            }
            array != null -> {
                numberOfUsedArrayEntries
            }
            hashSet != null -> {
                hashSet!!.size
            }
            else -> {
                0
            }
        }

    override fun iterator(): MutableIterator<V> {
        singleton?.let {
            return object : MutableIterator<V> {
                var done = false
                override fun hasNext(): Boolean {
                    return !done
                }

                override fun next(): V {
                    if (done) {
                        throw NoSuchElementException()
                    }
                    done = true
                    return it
                }

                override fun remove() {
                    if (done && singleton != null) {
                        singleton = null
                    } else {
                        throw IllegalStateException()
                    }
                }
            }
        }

        array?.let {
            return ArrayIterator()
        }

        hashSet?.let {
            return it.iterator()
        }
        return object : MutableIterator<V> {
            override fun hasNext(): Boolean {
                return false
            }

            override fun next(): V {
                throw NoSuchElementException()
            }

            override fun remove() {
                throw IllegalStateException()
            }

        }
    }

    override fun remove(element: V): Boolean {
        singleton?.let {
            if (it == element) {
                singleton = null
                return true
            }
            return false
        }

        array?.let {
            for (i in 0 until ARRAY_SIZE) {
                if (it[i] != null && it[i] == element) {
                    it[i] = null
                    numberOfUsedArrayEntries--
                    return true
                }
            }
            return false
        }

        hashSet?.let {
            return it.remove(element)
        }
        return false
    }

    override fun removeAll(elements: Collection<V>): Boolean {
        var changed = false
        for (o in elements) {
            changed = changed or remove(o)
        }
        return changed
    }

    override fun retainAll(elements: Collection<V>): Boolean {
        var changed = false
        val it = iterator()
        while (it.hasNext()) {
            if (!elements.contains(it.next())) {
                it.remove()
                changed = true
            }
        }
        return changed
    }

    override fun hashCode(): Int {
        singleton?.let {
            return it.hashCode()
        }
        array?.let {
            var h = 0
            for (i in 0 until ARRAY_SIZE) {
                it[i]?.let { element ->
                    h += element.hashCode()
                }
            }
            return h
        }
        hashSet?.let {
            return it.hashCode()
        }
        return 0
    }

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other !is MutableSet<*> || size != other.size || hashCode() != other.hashCode() -> false
            else -> containsAll(other)
        }
        TODO()
    }

    override fun toString(): String {
        singleton?.let {
            return "[$it]"
        }
        array?.let {
            return "[$it]"
        }
        hashSet?.let {
            return it.toString()
        }
        return "[]"
    }

    private inner class ArrayIterator : MutableIterator<V> {
        var next = 0
        var last = -1

        init {
            findNext()
        }

        private fun findNext() {
            while (next < ARRAY_SIZE && array!![next] == null) {
                next++
            }
        }

        override fun hasNext(): Boolean {
            return next < ARRAY_SIZE
        }

        override fun next(): V {
            if (next == ARRAY_SIZE) {
                throw NoSuchElementException()
            }
            last = next
            val v = array!![next++]
            findNext()
            return v!!
        }

        override fun remove() {
            array?.let {
                if (last == -1 || it[last] == null) {
                    throw IllegalStateException()
                }
                it[last] = null
                numberOfUsedArrayEntries--
                findNext()
            }
        }


    }

}