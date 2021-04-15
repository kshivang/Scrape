package headspaceBlogs.level_1

import com.github.kittinunf.fuel.core.awaitResponseResult
import com.github.kittinunf.fuel.gson.gsonDeserializerOf
import com.github.kittinunf.fuel.httpPost
import common.writeToCSV
import it.skrape.core.htmlDocument
import it.skrape.selects.html5.a
import it.skrape.selects.html5.article


const val api = "https://www.headspace.com/blog/wp-admin/admin-ajax.php"
const val writeFilesAt = "src/main/resources/headspaceBlogs/level_1/output.csv"


suspend fun main() {
    (1..345).map {
        it.fetchData()
            ?.findLink()
    }.flatMap {
        it?.asIterable() ?: listOf()
    }.map {
        listOf(it)
    }.writeToCSV(writeFilesAt)
}

suspend fun Int.fetchData() =
    (api.httpPost(listOf("action" to "get_posts", "page" to this))
        .awaitResponseResult(gsonDeserializerOf(Response::class.java)).third.component1()?.html)

fun String.findLink() =
    htmlDocument(this) {
        article {
            findAll {
                map {
                    it.a {
                        findFirst {
                            eachHref.first()
                        }
                    }
                }
            }
        }
    }

data class Response(
    val html: String,
    val pages: Int,
)