package dataflow.analysis.lattice

import soot.tagkit.Tag

class DataFlowTag<Node, Domain> constructor(
        private val name: String,
        val dataFlowMap: Map<Node, Domain>
): Tag {

    override fun getName(): String {
        return name
    }

    override fun getValue(): ByteArray {
        throw RuntimeException("DataFlowTag has no value for bytecode")
    }
}