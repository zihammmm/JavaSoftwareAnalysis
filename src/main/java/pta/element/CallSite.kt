package pta.element


import callgraph.CallKind
import pta.statement.Call
import util.AnalysisException

interface CallSite {
    val isInterface: Boolean

    val isVirtual: Boolean

    val isSpecial: Boolean

    val isStatic: Boolean

    var call: Call?

    var method: Method?

    var receiver: Variable?

    var arguments: MutableList<Variable>

    var containerMethod: Method?
}

fun CallSite.callKind(): CallKind {
    return when {
        isInterface -> CallKind.INTERFACE
        isVirtual -> CallKind.VIRTUAL
        isSpecial -> CallKind.SPECIAL
        isStatic -> CallKind.STATIC
        else -> throw AnalysisException("Unknown call site: $this")
    }
}