package pta.analysis.ci

class WorkList {
    private val pointerEntries = ArrayDeque<Entry>()

    inner class Entry constructor(
        val pointer: AbstractPointer
    ) {

    }
}