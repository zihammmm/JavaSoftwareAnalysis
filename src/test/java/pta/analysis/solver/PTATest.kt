package pta.analysis.solver

import org.junit.jupiter.api.Test
import test
import util.resultchecker.PAResultChecker

internal class PTATest {
    private val checker = PAResultChecker

    private fun testPTA(className: String, options: Array<String>) {
        test(checker, className, "pta", options)
    }

    @Test
    fun testOneCall() {
        testPTA("OneCall", arrayOf("-p", "wjtp.pta", "cs:1-call"))
    }

    @Test
    fun testOneObject() {
        testPTA("OneObject", arrayOf("-p", "wjtp.pta", "cs:1-obj"))
    }

    @Test
    fun testOneType() {
        testPTA("OneType", arrayOf("-p", "wjtp.pta", "cs:1-type"))
    }

    @Test
    fun testTwoCall() {
        testPTA("TwoCall", arrayOf("-p", "wjtp.pta", "cs:2-call"))
    }

    @Test
    fun testTwoObject() {
        testPTA("TwoObject", arrayOf("-p", "wjtp.pta", "cs:2-obj"))
    }

    @Test
    fun testTwoType() {
        testPTA("TwoType", arrayOf("-p", "wjtp.pta", "cs:2-type"))
    }
}