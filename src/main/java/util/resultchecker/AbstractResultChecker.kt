package util.resultchecker

import dataflow.analysis.constprop.main
import pta.analysis.solver.PointerAnalysisTransformer
import soot.G
import soot.Unit
import soot.UnitPatchingChain
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

/**
 * 结果检查抽象类
 */
abstract class AbstractResultChecker<K, V>{
    protected val resultMap = TreeMap<K, V>()

    protected val mismatches = TreeSet<String>()

    abstract fun readExpectedResult(filePath: Path)

    fun check(args: Array<String>, path: String): Set<String> {
        readExpectedResult(Paths.get(path))
        PointerAnalysisTransformer.output = false
        G.reset()
        main(args)
        return mismatches
    }

    protected fun isLastUnitOfItsLine(units: UnitPatchingChain, unit: Unit): Boolean {
        val succ = units.getSuccOf(unit)
        return succ == null || succ.javaSourceStartLineNumber != unit.javaSourceStartLineNumber
    }


    protected fun isMethodSignature(line: String) = line.startsWith("<")

    protected fun isEmpty(line: String) = line.trim() == ""


}