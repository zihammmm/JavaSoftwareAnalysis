package pta.analysis.context

import pta.element.CallSite

/**
 * K-callsite sensitive
 */
class CallsiteSensitive constructor(
    val k: Int
): Context<CallSite>(k) {

}