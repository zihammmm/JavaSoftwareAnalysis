package pta.jimple

import pta.analysis.ProgramManager
import pta.element.CallSite
import pta.element.Method
import pta.element.Type
import soot.Scene

class JimpleProgramManager: ProgramManager {
    private val hierarchy = Scene.v().orMakeFastHierarchy
    private val elementManager = ElementManager()

    override fun getEntryMethods(): Collection<Method> {

    }

    override fun canAssign(from: Type, to: Type): Boolean {
        TODO("Not yet implemented")
    }

    override fun resolveInterfaceOrVirtualCall(recvType: Type, method: Method) {
        TODO("Not yet implemented")
    }

    override fun resolveSpecialCall(callSite: CallSite, container: Method) {
        TODO("Not yet implemented")
    }
}