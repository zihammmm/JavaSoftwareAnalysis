package dataflow.analysis.constprop

import soot.PackManager
import soot.Transform
import soot.options.Options

fun main(args: Array<String>) {
    Options.v().set_src_prec(Options.src_prec_java)
    Options.v().set_output_format(Options.output_format_jimple)
    Options.v().set_prepend_classpath(true)

    PackManager.v()
            .getPack("jtp")
            .add(Transform("jtp.constprop", ConstantPropagation))

    soot.Main.main(args)
}