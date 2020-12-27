package pta.analysis.data

import pta.analysis.context.IContext
import pta.element.*
import pta.set.PointsToSetFactory
import util.HybridArrayHashMap

class MapBasedDataManager constructor(
    private var setFactory: PointsToSetFactory
) : DataManager {
    private val vars: MutableMap<Variable, MutableMap<IContext, CSVariable>> = HashMap()
    private val instanceFields: MutableMap<CSObj, MutableMap<Field, InstanceField>> = HashMap()
    private val arrayFields = HashMap<CSObj, ArrayField>()
    private val staticFields = HashMap<Field, StaticField>()
    private val objs: MutableMap<Obj, MutableMap<IContext, CSObj>> = HashMap()
    private val callSites: MutableMap<CallSite, MutableMap<IContext, CSCallSite>> = HashMap()
    private val methods: MutableMap<Method, MutableMap<IContext, CSMethod>> = HashMap()

    override fun setPointsToSetFactory(factory: PointsToSetFactory) {
        setFactory = factory
    }

    private fun <P : Pointer> initializePointsToSet(pointer: P): P {
        pointer.pointsToSet = setFactory.makePointsToSet()
        return pointer
    }

    override fun getCSVariable(context: IContext, variable: Variable): CSVariable {
        return vars.getOrCreateCSElement(variable, context) { v, c ->
            initializePointsToSet(CSVariable(v, c))
        }
    }

    override fun getInstanceField(base: CSObj, field: Field): InstanceField {
        return instanceFields.getOrCreateCSElement(base, field) { b, f ->
            initializePointsToSet(InstanceField(b, f))
        }
    }

    override fun getArrayField(array: CSObj): ArrayField {
        return arrayFields.getOrPut(array) {
            initializePointsToSet(ArrayField(array))
        }
    }

    override fun getStaticField(field: Field): StaticField {
        return staticFields.getOrPut(field) {
            initializePointsToSet(StaticField(field))
        }
    }

    override fun getCSObj(context: IContext, obj: Obj): CSObj {
        return objs.getOrCreateCSElement(obj, context) {o, c ->
            CSObj(o, c)
        }
    }

    override fun getCSCallSite(context: IContext, callSite: CallSite): CSCallSite {
        return callSites.getOrCreateCSElement(callSite, context) { cs, ct ->
            CSCallSite(cs, ct)
        }
    }

    override fun getCSMethod(context: IContext, method: Method): CSMethod {
        return methods.getOrCreateCSElement(method, context) {m, c ->
            CSMethod(m, c)
        }
    }

    override fun getCSVariables(): Sequence<CSVariable> {
        return vars.values.asSequence()
            .flatMap { it.values }
    }

    override fun getInstanceFields(): Sequence<InstanceField> {
        return instanceFields.values.asSequence()
            .flatMap { it.values }
    }
}

fun <R, K1, K2> MutableMap<K1, MutableMap<K2, R>>.getOrCreateCSElement(key1: K1, key2: K2, function: (K1, K2) -> R): R {
    return getOrPut(key1) {
        HybridArrayHashMap()
    }.getOrElse(key2) {
        function(key1, key2)
    }
}