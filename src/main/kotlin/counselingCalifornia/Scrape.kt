package counselingCalifornia

import common.writeToCSV
import it.skrape.core.htmlDocument
import it.skrape.selects.html5.a
import java.io.File
import java.lang.Exception

const val readFilesAt = "src/main/resources/counselingCalifornia/input"
const val writeFilesAt = "src/main/resources/counselingCalifornia/output"


fun main() {
    try {
        File(readFilesAt).listFiles { _, name ->
            name.endsWith(".html")
        }?.forEach { file ->
            val parsed = file.parse()
            val names = parsed.filter { !arrayOf("View", "Email").contains(it.first) }.map { it.first }
            val emails = parsed.filter { it.first == "Email" }.map { it.second[0].removePrefix("mailto:") }
            val writeTo = file.name.replace("html", "csv")
            names.mapIndexed { index, name -> listOf(name, emails[index]) }
                .writeToCSV("$writeFilesAt/${writeTo}")
        }
    } catch (e: Exception) {
        main()
    }
}

fun File.parse() =
    htmlDocument(this) {
        a {
            findAll {
                filter {
                    it.attribute("target") == "_top"
                }.map {
                    it.html to it.eachHref
                }
            }
        }
    }
