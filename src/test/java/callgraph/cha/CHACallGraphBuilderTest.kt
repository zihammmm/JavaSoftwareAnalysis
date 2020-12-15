package callgraph.cha

import org.junit.jupiter.api.Test
import test
import util.resultchecker.CHAResultChecker

internal class CHACallGraphBuilderTest {
    private val resultCheck = CHAResultChecker

    @Test
    fun testStaticCall() {
        test(resultCheck, "StaticCall", "CHA")
    }

    @Test
    fun testSpecialCall() {
        test(resultCheck, "SpecialCall", "CHA")
    }

    @Test
    fun testVirtualCall() {
        test(resultCheck, "VirtualCall", "CHA")
    }

    @Test
    fun testInterface() {
        test(resultCheck, "Interface", "CHA")
    }
}