package headspaceBlogs.level_2

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.kittinunf.fuel.httpGet
import common.writeToCSV
import it.skrape.core.htmlDocument
import it.skrape.selects.html5.div
import java.io.File

const val readFileAt = "src/main/resources/headspaceBlogs/level_1/output.csv"
const val writeFileAt = "src/main/resources/headspaceBlogs/level_2/output.csv"
const val startAt = 0
const val endAt = 1034

fun main() {
    var count = 0
    csvReader().readAll(File(readFileAt)).forEach { linkList ->
        if (count >= startAt) {
            if (count > endAt) {
                println("completed!")
                return
            }
            println("At index $count")
            try {
                linkList.first().httpGet().responseString().third.component1()
                    ?.parse()
                    ?.let {
                        listOf(it).writeToCSV(writeFileAt, true)
                    }
            } catch (e: Exception) {
                println("Exception for ${linkList.first()}")
            }
        }
        count++
    }
}

fun String.parse() =
    listOf(
        htmlDocument(this) {
            titleText
        }.toString(),
        htmlDocument(this) {
            div {
                withClass = "content__inner"
                findFirst {
                    this
                }
            }
        }.toString()
    )