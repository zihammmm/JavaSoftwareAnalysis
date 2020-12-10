import java.io.File

object TestUtils {
    fun test(className: String) {
        val cp = if (File("analyzed/constprop/").exists()) {
            "analyzed/constprop/"
        } else {
            "analyzed/"
        }


    }
}