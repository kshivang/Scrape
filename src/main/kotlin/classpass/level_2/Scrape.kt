package classpass.level_2

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import it.skrape.core.fetcher.HttpFetcher
import it.skrape.core.htmlDocument
import it.skrape.extract
import it.skrape.skrape
import java.io.File
import java.lang.Exception

const val readFilesAt = "src/main/resources/classpass/level_1"
const val writeFilesAt = "src/main/resources/classpass/level_2"

const val website_prefix = "https://classpass.com/studios"

const val startFrom = 0
const val limit = 100

fun main() {
    File(readFilesAt).listFiles { _, name -> name.endsWith(".csv") }
        ?.map {
            csvReader().readAll(it)
        }?.flatten()
//        ?.subList(startFrom, limit)
        ?.let {
            val size = it.size
            var count = 0
            it.map { fields ->
                scrapeLinks(fields.first()).run {
                    val phoneNo = firstOrNull { link ->
                        link.contains("tel:")
                    } ?: ""
                    val instagramLink = firstOrNull { link ->
                        link.contains("instagram")
                    } ?: ""

                    val facebookLink = firstOrNull { link ->
                        link.contains("facebook")
                    } ?: ""

                    val twitterLink = firstOrNull { link ->
                        link.contains("twitter")
                    } ?: ""

                    val websiteLink = firstOrNull { link ->
                        link != instagramLink && link != facebookLink && link != twitterLink && link != phoneNo
                    } ?: ""

                    listOf(instagramLink, facebookLink, twitterLink, phoneNo, websiteLink) + fields
                }.let {
                    println("${++count} out of $size")
                    listOf(it).writeToCSV("$writeFilesAt/all.csv")
                }
            }
        }
}

fun List<List<String>>.writeToCSV(filePath: String) = csvWriter().writeAll(this, filePath, true)

fun scrapeLinks(alias: String): List<String> {
    return skrape(HttpFetcher) {
        request {
            url = "$website_prefix/$alias"
        }
        extract {
            htmlDocument {
                try {
                    findAll("._2MTzNd_HEKWjVL824SA9Li") {
                        map {
                            it.eachHref
                        }.flatten().filter { !it.contains("www.google") }
                    }
                } catch (e: Exception) {
                    listOf()
                }
            }
        }
    }
}

