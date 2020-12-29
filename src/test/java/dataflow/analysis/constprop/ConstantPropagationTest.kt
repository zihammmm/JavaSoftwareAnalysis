package dataflow.analysis.constprop

import org.junit.jupiter.api.Test
import test
import util.resultchecker.CPResultChecker


internal class ConstantPropagationTest {
    private val resultCheck = CPResultChecker

    @Test
    fun testSimpleConstant() {
        test(resultCheck, "SimpleConstant", "constprop", emptyArray())
    }

    @Test
    fun testSimpleBinary() {
        test(resultCheck, "SimpleBinary", "constprop", emptyArray())
    }

    @Test
    fun testSimpleBranch() {
        test(resultCheck, "SimpleBranch", "constprop", emptyArray())
    }
}