package pta.analysis.context

import pta.element.Type

class TypeSensitive constructor(
    override val capability: Int
) : IContext<Type> {

}