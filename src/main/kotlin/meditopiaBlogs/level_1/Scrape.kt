package meditopiaBlogs.level_1

import com.github.kittinunf.fuel.core.awaitResponse
import com.github.kittinunf.fuel.gson.gsonDeserializerOf
import com.github.kittinunf.fuel.httpGet

const val api =
    "https://blog.meditopia.com/wp-admin/admin-ajax.php?columns=1&viewStyle=style_3&offset=0&args={%22post_type%22:%22post%22,%22post_status%22:%22publish%22,%22posts_per_page%22:%222000%22,%22offset%22:0,%22orderby%22:%22date%22,%22order%22:%22desc%22,%22tax_query%22:[{%22taxonomy%22:%22category%22,%22field%22:%22id%22,%22terms%22:%22108%22}]}&perPage=2000&action=stmt_ajax_get_grid_news"

suspend fun main() {
    api.httpGet().awaitResponse(gsonDeserializerOf(Response::class.java)).third.let {
        println("response ${it.html}")
    }
}

data class Response(val html: String)
