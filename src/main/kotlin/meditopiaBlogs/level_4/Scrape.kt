package meditopiaBlogs.level_4

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import common.writeToCSV
import org.jsoup.Jsoup
import java.io.File

const val readFileAt = "src/main/resources/meditopiaBlogs/level_3/output.csv"
const val writeFileAt = "src/main/resources/meditopiaBlogs/level_4/output.csv"

fun main() {
    csvReader().readAll(File(readFileAt))
        .map { listOf(it[0], it[1].clean()) }
        .writeToCSV(writeFileAt)

}

fun String.clean() = Jsoup.parse(this).run {
    select("img").remove()
    select("a").remove()
    toString()
}