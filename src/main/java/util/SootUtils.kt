package util

import soot.LabeledUnitPrinter
import soot.Unit
import soot.jimple.GotoStmt

fun unitToString(up: LabeledUnitPrinter, unit: Unit): String {
    val string = "L${unit.javaSourceStartLineNumber}{${up.labels()[unit]}:"
    return if (unit is GotoStmt) {
        "${string}goto${up.labels()[unit.target]}}"
    }else {
        "${string}${unit}}"
    }
}