package pta.element


import pta.statement.Call

interface CallSite {
    val isInterface: Boolean

    val isVirtual: Boolean

    val isSpecial: Boolean

    val isStatic: Boolean

    var call: Call?

    var method: Method?

    var receiver: Variable?

    var arguments: MutableList<Variable>?

    var containerMethod: Method?
}