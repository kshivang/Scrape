package meditopiaBlogs.level_2

import com.google.gson.Gson
import common.writeToCSV
import it.skrape.core.htmlDocument
import it.skrape.selects.html5.a
import it.skrape.selects.html5.h3
import java.io.File

const val readFileAt = "src/main/resources/meditopiaBlogs/level_1/response.json"
const val writeFileAt = "src/main/resources/meditopiaBlogs/level_2/output.csv"

fun main() {
    Gson().fromJson(File(readFileAt).bufferedReader().readText(), Response::class.java)
        .html.parseLinks().writeToCSV(writeFileAt)
}

fun String.parseLinks() = htmlDocument(this) {
    h3 {
        withClass = "heading-title"
        a {
            findAll {
                map {
                    listOf(it.eachHref.first())
                }
            }
        }
    }
}

data class Response(val html: String)
