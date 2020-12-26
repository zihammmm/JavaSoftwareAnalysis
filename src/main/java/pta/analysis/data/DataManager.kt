package pta.analysis.data

import pta.analysis.context.IContext
import pta.analysis.context.ObjectSensitive
import pta.element.*
import pta.set.PointsToSetFactory

interface DataManager {
    fun setPointsToSetFactory(factory: PointsToSetFactory)

    fun getCSVariable(context: IContext, variable: Variable): CSVariable

    fun getInstanceField(obj: ObjectSensitive, field: Field): InstanceField

    fun getArrayField(obj: ObjectSensitive): ArrayField

    fun getStaticField(field: Field): StaticField

    fun getCSObj(context: IContext, obj: Obj): CSObj

    fun getCSCallsite(context: IContext, callsite: CallSite): CSCallsite

    fun getCSMethod(context: IContext, method: Method): CSMethod

    fun getCSVariables(): Sequence<CSVariable>

    fun getInstanceFields(): Sequence<InstanceField>
}