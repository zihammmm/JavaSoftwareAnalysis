package callgraph.cha

import org.junit.jupiter.api.Test
import test
import util.resultchecker.CHAResultChecker

internal class CHACallGraphBuilderTest {
    private val resultCheck = CHAResultChecker

    @Test
    fun testStaticCall() {
        test(resultCheck, "StaticCall", "CHA", emptyArray())
    }

    @Test
    fun testSpecialCall() {
        test(resultCheck, "SpecialCall", "CHA", emptyArray())
    }

    @Test
    fun testVirtualCall() {
        test(resultCheck, "VirtualCall", "CHA", emptyArray())
    }

    @Test
    fun testInterface() {
        test(resultCheck, "Interface", "CHA", emptyArray())
    }
}