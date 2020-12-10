package dataflow.analysis.constprop

import dataflow.analysis.lattice.FlowMap
import soot.Local

class FlowMap: LinkedHashMap<Local, Value>(), FlowMap<Local, Value>{
    override fun update(k: Local, v: Value): Boolean {
        return put(k, v) != v
    }

    override fun get(key: Local): Value {
        return getOrDefault(key, Value.UNDEF)
    }
}