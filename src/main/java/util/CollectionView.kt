package util

interface CollectionView<From, To>: Collection<To> {
    companion object {
        fun <From, To> of(collection: Collection<From>, mapper:(From) -> To): CollectionView<From, To> {
            return ImmutableCollectionView(collection, mapper)
        }
    }
}
