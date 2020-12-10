package dataflow.analysis.constprop

import util.AnalysisException

class Value constructor(
    private val kind: Kind,
    private val value: Int
) {

    constructor(kind: Kind) : this(kind, 0)

    val isNAC = kind == Kind.NAC

    val isConstant = kind == Kind.CONSTANT

    val isUndef = kind == Kind.UNDEF

    fun getConstant(): Int {
        if (isConstant) {
            return value
        } else {
            throw AnalysisException("$this is not a constant")
        }
    }

    override fun hashCode(): Int {
        return value
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other !is Value -> false
            else -> kind == other.kind && value == other.value
        }
    }

    override fun toString(): String {
        return when(kind) {
            Kind.NAC -> "NAC"
            Kind.CONSTANT -> value.toString()
            Kind.UNDEF -> "UNDEF"
        }
    }

    companion object {
        val NAC = Value(Kind.NAC)
        val UNDEF = Value(Kind.UNDEF)

        fun makeConstant(v: Int) = Value(Kind.CONSTANT, v)
    }

    enum class Kind {
        NAC,
        CONSTANT,
        UNDEF
    }
}