package callgraph

import soot.SootMethod
import soot.Unit
import soot.jimple.Stmt
import util.addToMapSet

class JimpleCallGraph: AbstractCallGraph<Unit, SootMethod>() {
    /**
     * Add a new method to call graph
     * return true if the method was not in this call graph
     */
    override fun addNewMethod(method: SootMethod): Boolean {
        if (reachableMethods.add(method)) {
            if (method.isConcrete) {
                for(unit in method.retrieveActiveBody().units) {
                    val stmt = unit as Stmt
                    if (stmt.containsInvokeExpr()) {
                        callSiteToContainer[stmt] = method
                        callSiteIn.addToMapSet(method, stmt)
                    }
                }
            }
            return true
        }
        return false
    }
}