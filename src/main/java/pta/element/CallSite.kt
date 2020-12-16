package pta.element


import pta.statement.Call

interface CallSite {
    val isInterface: Boolean

    val isVirtual: Boolean

    val isSpecial: Boolean

    val isStatic: Boolean

    fun getCall(): Call

    fun getMethod(): Method

    fun getReceiver(): Variable

    fun getArguments(): List<Variable>

    fun getContainerMethod(): Method
}