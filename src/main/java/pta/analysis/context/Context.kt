package pta.analysis.context

import java.lang.IllegalArgumentException

/**
 * k - T-Context Sensitivity
 */
open class Context<T> constructor(
    private val capability: Int = 0
) : IContext {
    private val elementList: ArrayDeque<T>
        get() = ArrayDeque(capability)

    override val depth: Int
        get() = elementList.size

    fun element(index: Int): T {
        if (index < depth) {
            return elementList[index]
        }
        throw IllegalArgumentException(
            "Context $this doesn't have $index-th element"
        )
    }

    fun addElement(element: T): Boolean {
        if(capability == 0) {
            throw IllegalStateException("The Capability is 0!")
        }
        while (depth < capability) {
            elementList.removeFirst();
        }
        return elementList.add(element)
    }
}