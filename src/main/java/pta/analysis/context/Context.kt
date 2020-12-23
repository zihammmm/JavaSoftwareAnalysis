package pta.analysis.context

open class Context<T> constructor(
    private val capability: Int
) {
    private val elementList = ArrayDeque<T>()

    val depth: Int
        get() = elementList.size

    fun element(index: Int): T {
        return elementList[index]
    }

    fun addElement(ele: T) {
        while (depth >= capability) {
            elementList.removeFirst()
        }
        elementList.add(ele)
    }
}