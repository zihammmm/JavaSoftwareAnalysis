import util.resultchecker.AbstractResultChecker
import java.io.File
import kotlin.test.assertTrue

fun <K,V> test(resultChecker: AbstractResultChecker<K, V>, className: String, dir: String) {
    val cp = if (File("analyzed/$dir/").exists()) {
        "analyzed/$dir/"
    } else {
        "analyzed/"
    }
    val mismatches = resultChecker.check(arrayOf("-cp", cp, className), "$cp$className-expected.txt")
    assertTrue(mismatches.isEmpty())
}
