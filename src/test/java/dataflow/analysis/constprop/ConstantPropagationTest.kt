package dataflow.analysis.constprop

import org.junit.jupiter.api.Test
import test
import util.resultchecker.CPResultChecker


internal class ConstantPropagationTest {
    private val resultCheck = CPResultChecker

    @Test
    fun testSimpleConstant() {
        test(resultCheck, "SimpleConstant", "constprop")
    }

    @Test
    fun testSimpleBinary() {
        test(resultCheck, "SimpleBinary", "constprop")
    }

    @Test
    fun testSimpleBranch() {
        test(resultCheck, "SimpleBranch", "constprop")
    }
}