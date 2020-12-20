package pta.jimple

import pta.analysis.ProgramManager
import pta.element.CallSite
import pta.element.Method
import pta.element.Type
import soot.Scene
import soot.jimple.SpecialInvokeExpr
import java.util.*

class JimpleProgramManager: ProgramManager {
    private val hierarchy = Scene.v().orMakeFastHierarchy
    val elementManager = ElementManager()

    override fun getEntryMethods(): Collection<Method> {
        return Collections.singleton(elementManager.getMethod(Scene.v().mainMethod))
    }

    override fun canAssign(from: Type, to: Type): Boolean {
        return hierarchy.canStoreType((from as JimpleType).sootType, (to as JimpleType).sootType)
    }

    override fun resolveInterfaceOrVirtualCall(recvType: Type, method: Method): Method {
        val jType = recvType as JimpleType
        val jMethod = method as JimpleMethod
        val callee = hierarchy.resolveConcreteDispatch(jType.sootClass, jMethod.method)
        return elementManager.getMethod(callee)
    }

    override fun resolveSpecialCall(callSite: CallSite, container: Method): Method {
        val jCallSite = callSite as JimpleCallSite
        val jContainer = container as JimpleMethod
        val callee = hierarchy.resolveSpecialDispatch(jCallSite.sootInvokeExpr as SpecialInvokeExpr, jContainer.method)
        return elementManager.getMethod(callee)
    }

}