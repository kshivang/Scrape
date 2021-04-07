package classpass.level_1

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.github.kittinunf.fuel.core.awaitResponseResult
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.gson.gsonDeserializerOf
import com.github.kittinunf.fuel.httpPost
import common.writeToCSV

const val api = "https://classpass.com/_api/unisearch/v1/layout/web_search_page"
const val outputPath = "src/main/resources/classpass/level_1"

enum class Params(val fileName: String, var param: String) {
    UK("$outputPath/unitedKingdom.csv", "{\"search_request\":{\"filters\":{\"date\":\"2020-12-07\",\"lat\":55.378051,\"lon\":-3.435973,\"place_id\":\"ChIJqZHHQhE7WgIReiWIMkOg-MQ\",\"result_type\":\"VENUE\",\"tag\":[],\"map_bounds\":\"-38.89210225382087,15.447129584880548,32.020156253819025,80.11963274608343\"},\"venue_search_options\":{\"page_size\":10000,\"include_map_items\":false}}}"),
    US("$outputPath/unitedStates.csv", "{\"search_request\":{\"filters\":{\"date\":\"2020-12-07\",\"lat\":37.09024,\"lon\":-95.712891,\"place_id\":\"ChIJCzYy5IS16lQRQrfeQ5K5Oxw\",\"result_type\":\"VENUE\",\"tag\":[],\"map_bounds\":\"-169.11588510331222,13.035153510579796,-22.309896896687885,61.145326489420654\"},\"venue_search_options\":{\"page_size\":9000,\"include_map_items\":false}}}"),
    AUS("$outputPath/australia.csv", "{\"search_request\":{\"filters\":{\"date\":\"2020-12-07\",\"lat\":-25.274398,\"lon\":133.775136,\"place_id\":\"ChIJ38WHZwf9KysRUhNblaFnglM\",\"result_type\":\"VENUE\",\"tag\":[],\"map_bounds\":\"89.3656849711971,-78.83788550157313,178.18458702880525,51.92205809665459\"},\"venue_search_options\":{\"page_size\":5000,\"include_map_items\":false}}}"),
    CAN("$outputPath/canada.csv", "{\"search_request\":{\"filters\":{\"date\":\"2020-12-07\",\"lat\":56.130366,\"lon\":-106.346771,\"place_id\":\"ChIJ2WrMN9MDDUsRpY9Doiq3aJk\",\"result_type\":\"VENUE\",\"tag\":[],\"map_bounds\":\"-155.8914726844574,-10.12878183915655,-56.80206931554109,86.88183265193135\"},\"venue_search_options\":{\"page_size\":9000,\"include_map_items\":false}}}")
}


suspend fun main() {
    Params.UK.apply {
        getData().writeToCSV(fileName)
    }
    Params.US.apply {
        getData().writeToCSV(fileName)
    }
    Params.AUS.apply {
        getData().writeToCSV(fileName)
    }
    Params.CAN.apply {
        getData().writeToCSV(fileName)
    }
}

suspend fun Params.getData() =
    (api.httpPost()
        .jsonBody(param)
        .awaitResponseResult(gsonDeserializerOf(Response::class.java))
        .third
        .component1()
        ?.data
        ?.modules
        ?.web_search_results_01
        ?.data
        ?.venue_tab_items
        ?.map { it.list } ?: listOf())
        .run { listOf(VenueTabItem.header) + this }


data class Response(
    val data: Data,
)

data class Data(
    val modules: Modules,
)

data class Modules(
    val web_search_results_01: WebSearchResults,
)

data class WebSearchResults(
    val data: ActualData,
)

data class ActualData(
    val cursor: String,
    val search_id: String,
    val venue_tab_items: List<VenueTabItem>
)

data class VenueTabItem(
    val alias: String,
    val activities: String,
    val address: Address,
    val venue_id: Int,
    val venue_name: String,
    val location_name: String,
    val display_rating_total: String,
    val display_rating_average: Double,
//    val description: String,
) {

    val list get() = fields.map {
        it.get(this).toString()
    }

    companion object {
        private val fields get() = VenueTabItem::class.java.declaredFields
            .filter { it.name != "Companion" }

        val header get() = fields.map { it.name }
    }
}

data class Address(
    val address_line0: String,
    val address_line1: String,
    val address_line2: String,
    val city: String,
    val state: String,
    val zip_code: String,
)
