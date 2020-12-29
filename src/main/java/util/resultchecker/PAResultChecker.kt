package util.resultchecker

import pta.analysis.data.Pointer
import pta.analysis.solver.PointerAnalysis
import pta.analysis.solver.PointerAnalysisTransformer
import soot.G
import soot.Main
import util.sequenceToString
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

object PAResultChecker: AbstractResultChecker<String, String>() {

    val available = true

    override fun readExpectedResult(filePath: Path) {
        try {
            for (line in Files.readAllLines(filePath)) {
                if (isPointsToSet(line)) {
                    val splits = line.split(" -> ")
                    val pointer = splits[0]
                    val pts = splits[1]
                    resultMap[pointer] = pts
                }
            }
        } catch (e: IOException) {
            throw RuntimeException("Failed to read $filePath caused by $e")
        }
    }

    private fun isPointsToSet(line: String) = line.startsWith("[")

    private fun comparePointer(p: Pointer, givenPointers: MutableSet<String>) {
        val ptr = p.toString()
        val given = p.pointsToSet!!.asSequence().sequenceToString()
        val expected = resultMap[ptr]
        if (given == expected) {
            mismatches.add("\n$ptr, expected: $expected, given: $given")
        }
        givenPointers.add(ptr)
    }

    fun compare(pta: PointerAnalysis) {
        val givenPointers = TreeSet<String>()
        pta.variables
            .sortedWith(Comparator.comparing{it.toString()})
            .forEach { comparePointer(it, givenPointers) }
        pta.instanceFields
            .sortedWith(Comparator.comparing { it.base.toString() })
            .forEach { comparePointer(it, givenPointers) }
        resultMap.forEach { (p, pts) ->
            if (!givenPointers.contains(p)) {
                mismatches.add("\n$p, expected: $pts, given: pointer has not been added to PFG")
            }
        }
    }
}