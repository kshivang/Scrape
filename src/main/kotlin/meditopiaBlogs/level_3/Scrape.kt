package meditopiaBlogs.level_3

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.kittinunf.fuel.httpGet
import common.writeToCSV
import it.skrape.core.htmlDocument
import it.skrape.selects.html5.div
import java.io.File


const val readFileAt = "src/main/resources/meditopiaBlogs/level_2/output.csv"
const val writeFileAt = "src/main/resources/meditopiaBlogs/level_3/output.csv"
const val errorFileAt = "src/main/resources/meditopiaBlogs/level_3/error.csv"
const val startAt = 0
const val endAt = 139

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
                linkList.first().httpGet().responseString().third
                    .component1()
                    ?.parse()
                    ?.let {
                        listOf(it).writeToCSV(writeFileAt, true)
                    }
            } catch (e: Exception) {
                println("Exception for ${linkList.first()}, count: $count")
                listOf(linkList).writeToCSV(errorFileAt, true)
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
                withId = "bsf_rt_marker"
                findFirst {
                    this
                }
            }
        }.toString()
    )