package cms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import common.writeToCSV
import org.jsoup.Jsoup
import java.io.File

const val readFileAt = "src/main/resources/headspaceBlogs/level_3/input.csv"
const val writeFileAt = "src/main/resources/headspaceBlogs/level_3/output.csv"

fun main() {
    csvReader().readAll(File(readFileAt))
        .map { listOf(it[1], it[0].removeImgTags(), it[3]) }
        .writeToCSV(writeFileAt)

}

fun String.removeImgTags() = Jsoup.parse((this)).run {
    select("img").remove()
    select("a").remove()
    toString()
}



