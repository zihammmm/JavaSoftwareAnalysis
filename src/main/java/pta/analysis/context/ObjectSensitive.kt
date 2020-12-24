package pta.analysis.context

import pta.element.Obj

class ObjectSensitive constructor(
    override val capability: Int
): IContext<Obj> {

}