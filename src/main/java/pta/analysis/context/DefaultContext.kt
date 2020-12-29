package pta.analysis.context

/**
 * Context-InSensitive
 * the depth is 0
 */
object DefaultContext: IContext {
    override val depth: Int
        get() = 0
}
