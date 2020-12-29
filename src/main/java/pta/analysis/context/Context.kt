package pta.analysis.context

import java.lang.IllegalArgumentException

/**
 * k - T-Context Sensitivity
 */
open class Context<T> constructor(
    private val capability: Int = 2
) : IContext {
    private val elementList: ArrayDeque<T>
        get() = ArrayDeque(capability)

    override val depth: Int
        get() = elementList.size

    constructor(context: IContext) : this(context.depth) {
        if (context is Context<*>) {
            addElement(context)
        }
    }

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
        while (depth >= capability) {
            elementList.removeFirst();
        }
        return elementList.add(element)
    }

    fun addElement(context: IContext): Boolean {
        if (context !is Context<*>) {
            return false
        }
        var changed = false
        for (element in context.elementList) {
            changed = changed || addElement(element as T)
        }
        return changed
    }

    fun containsElement(element: T): Boolean = elementList.contains(element)
}