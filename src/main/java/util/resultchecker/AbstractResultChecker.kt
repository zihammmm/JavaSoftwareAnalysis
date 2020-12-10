package util.resultchecker

import soot.G
import soot.Unit
import soot.UnitPatchingChain
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

abstract class AbstractResultChecker<K, V>{
    protected val resultMap = TreeMap<K, V>()

    protected val mismatches = TreeSet<String>()

    abstract fun readExpectedResult(filePath: Path)

    fun check(args: Array<String>, path: String) {
        G.reset()
    }

    protected fun isLastUnitOfItsLine(units: UnitPatchingChain, unit: Unit): Boolean {
        return units.getSuccOf(unit).javaSourceStartLineNumber != unit.javaSourceStartLineNumber
    }


    protected fun isMethodSignature(line: String) = line.startsWith("<")

    protected fun isEmpty(line: String) = line.trim() == ""


}