package pta.analysis.context

import pta.element.CallSite

interface ContextSelector<T> {
    fun selectContext(callSite: CallSite)
}