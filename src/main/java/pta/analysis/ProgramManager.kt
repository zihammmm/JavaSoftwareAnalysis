package pta.analysis

import pta.element.CallSite
import pta.element.Method
import pta.element.Type

interface ProgramManager {
    fun getEntryMethods(): Collection<Method>

    fun canAssign(from: Type, to: Type): Boolean

    fun resolveInterfaceOrVirtualCall(recvType: Type, method: Method)

    fun resolveSpecialCall(callSite: CallSite, container: Method)
}