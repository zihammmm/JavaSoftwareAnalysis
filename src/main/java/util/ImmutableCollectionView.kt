package util

class ImmutableCollectionView<From, To>(
    private val collection: Collection<From>,
    private val mapper: (From) -> To,
): CollectionView<From, To> {

    override val size: Int
        get() = collection.size

    override fun contains(element: To): Boolean {
        throw UnsupportedOperationException("contains() currently is not supported")
    }

    override fun containsAll(elements: Collection<To>): Boolean {
        TODO("Not yet implemented")
    }

    override fun isEmpty() = collection.isEmpty()

    override fun iterator(): Iterator<To> {
        return object : Iterator<To> {
            private val iter: Iterator<From> = this@ImmutableCollectionView.collection.iterator()

            override fun hasNext() = iter.hasNext()

            override fun next(): To = mapper.run {
                this(iter.next())
            }

        }
    }

    fun toArray(): List<To> {
        return collection.asSequence()
            .map(mapper)
            .toList()
    }

}