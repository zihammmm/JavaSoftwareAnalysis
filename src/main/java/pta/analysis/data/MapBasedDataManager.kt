package pta.analysis.data

import pta.analysis.context.IContext
import pta.analysis.context.ObjectSensitive
import pta.element.*
import pta.set.HybridPointsToSet
import pta.set.PointsToSetFactory
import util.HybridArrayHashSet

class MapBasedDataManager constructor(
    private var setFactory: PointsToSetFactory
): DataManager {
    private val vars = HashMap<Variable, HashMap<IContext, CSVariable>>()
    private val instanceFields = HashMap<CSObj, HashMap<Field, InstanceField>>()
    private val arrayFields = HashMap<CSObj, ArrayField>()
    private val staticFields = HashMap<Field, StaticField>()
    private val objs = HashMap<Obj, HashMap<IContext, CSObj>>()
    private val callSites = HashMap<CallSite, HashMap<IContext, CSCallsite>>()
    private val methods = HashMap<Method, HashMap<IContext, CSMethod>>()

    companion object {
        private fun <R, K1, K2> getOrCreateCSElement(map: MutableMap<K1, MutableMap<K2, R>>, key1: K1, key2: K2, function: (K1, K2)->R): R {

        }
    }

    override fun setPointsToSetFactory(factory: PointsToSetFactory) {
        setFactory = factory
    }

    override fun getCSVariable(context: IContext, variable: Variable): CSVariable {

    }

    override fun getInstanceField(obj: ObjectSensitive, field: Field): InstanceField {
        TODO("Not yet implemented")
    }

    override fun getArrayField(obj: ObjectSensitive): ArrayField {
        TODO("Not yet implemented")
    }

    override fun getStaticField(field: Field): StaticField {
        TODO("Not yet implemented")
    }

    override fun getCSObj(context: IContext, obj: Obj): CSObj {
        TODO("Not yet implemented")
    }

    override fun getCSCallsite(context: IContext, callsite: CallSite): CSCallsite {
        TODO("Not yet implemented")
    }

    override fun getCSMethod(context: IContext, method: Method): CSMethod {
        TODO("Not yet implemented")
    }

    override fun getCSVariables(): Sequence<CSVariable> {
        TODO("Not yet implemented")
    }

    override fun getInstanceFields(): Sequence<InstanceField> {
        TODO("Not yet implemented")
    }
}