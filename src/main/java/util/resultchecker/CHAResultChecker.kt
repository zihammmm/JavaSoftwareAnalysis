package util.resultchecker

import callgraph.CallGraph
import callgraph.cha.CallGraphPrinter
import soot.*
import soot.Unit
import util.addToMapMap
import util.unitToString
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

object CHAResultChecker : AbstractResultChecker<String, MutableMap<String, String>>() {

    override fun readExpectedResult(filePath: Path) {
        var currentMethod = ""
        try {
            for (line in Files.readAllLines(filePath)) {
                if (isMethodSignature(line)) {
                    currentMethod = line
                } else if (!isEmpty(line)) {
                    val splits = line.split(" -> ")
                    val callUnit = splits[0]
                    val callees = splits[1]
                    resultMap.addToMapMap(currentMethod, callUnit, callees)
                }
            }
        } catch (e: IOException) {
            throw RuntimeException("Failed to read $filePath caused by $e")
        }
    }

    fun compare(body: Body, callGraph: CallGraph<Unit, SootMethod>) {
        val method = body.method.signature
        val expectedCallEdges = resultMap.getOrDefault(method, emptyMap())
        val up = BriefUnitPrinter(body)
        body.units.forEach {
            val callUnit = unitToString(up, it)
            val expected = expectedCallEdges[callUnit]
            val callees = callGraph.getCallees(it)
            val given = CallGraphPrinter.calleesToString(callees)
            if (expected != null) {
                if (expected != given) {
                    mismatches.add("\nCallees of $callUnit, expected: $expected, given: $given")
                }
            } else if (callGraph.getCallees(it).isNotEmpty()) {
                mismatches.add("\nCallees of $callUnit, expected: [], given: $given")
            }
        }
    }

}