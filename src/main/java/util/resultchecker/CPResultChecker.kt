package util.resultchecker

import dataflow.analysis.constprop.FlowMap
import soot.Body
import soot.Unit
import soot.jimple.NopStmt
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

object CPResultChecker: AbstractResultChecker<String, MutableMap<Int, MutableMap<String, String>>>() {

    fun compare(body: Body, result: Map<Unit, FlowMap>) {
        val method = body.method.signature
        val units = body.units
        units.forEach {
            val lineNumber = it.javaSourceStartLineNumber
            if (it !is NopStmt && isLastUnitOfItsLine(units, it)) {
                result[it]?.let { map ->
                    doCompare(method, lineNumber, map)
                }
            }
        }
    }

    private fun doCompare(method: String, lineNumber: Int, analysisResult: FlowMap) {
        val expectedResult = getValuesAt(method, lineNumber)
        expectedResult.forEach { (t, expected) ->
            var found = false
            for (entry in analysisResult.entries) {
                if (entry.key.name == t) {
                    found = true
                    val value = entry.value
                    if (value.toString() != expected) {
                        mismatches.add("\n${method}:L${lineNumber}, '${t}', expected: ${expected}, given: $value")
                    }
                }
            }
            if (!found && expected != "UNDEF") {
                mismatches.add("\n${method}:L${lineNumber}, '$t', expected: $expected, given: UNDEF")
            }
        }
    }

    override fun readExpectedResult(filePath: Path) {
        var currentMethod = ""
        try {
            for (line in Files.readAllLines(filePath)) {
                if (isMethodSignature(line)) {
                    currentMethod = line
                } else if (!isEmpty(line)) {
                    parseLine(currentMethod, line)
                }
            }
        }catch (e: IOException) {
            throw RuntimeException("Failed to read $filePath caused by $e")
        }
    }

    private fun parseLine(method: String, line: String) {
        val splits = line.split(":")
        val lineNumber = splits[0].toInt()
        val pairs = splits[1].split(",")
        for (pair in pairs) {
            val varValue = pair.split("=")
            addValue(method, lineNumber, varValue[0], varValue[1])
        }
    }

    private fun addValue(method: String, lineNumber: Int, variable: String, value: String) {
        resultMap.computeIfAbsent(method) { TreeMap() }
                .computeIfAbsent(lineNumber) { TreeMap() }
                .put(variable, value)?.let {
                    throw RuntimeException("Value of $method:$lineNumber:$variable already exists")
                }
    }

    private fun getValuesAt(method: String, lineNumber: Int): Map<String, String> {
        return resultMap.getOrDefault(method, emptyMap())
                .getOrDefault(lineNumber, emptyMap())
    }
}