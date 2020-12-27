package pta.analysis.data

import pta.analysis.context.IContext
import pta.element.*
import pta.set.PointsToSetFactory

interface DataManager {
    fun setPointsToSetFactory(factory: PointsToSetFactory)

    fun getCSVariable(context: IContext, variable: Variable): CSVariable

    fun getInstanceField(base: CSObj, field: Field): InstanceField

    fun getArrayField(array: CSObj): ArrayField

    fun getStaticField(field: Field): StaticField

    fun getCSObj(context: IContext, obj: Obj): CSObj

    fun getCSCallSite(context: IContext, callSite: CallSite): CSCallSite

    fun getCSMethod(context: IContext, method: Method): CSMethod

    fun getCSVariables(): Sequence<CSVariable>

    fun getInstanceFields(): Sequence<InstanceField>
}