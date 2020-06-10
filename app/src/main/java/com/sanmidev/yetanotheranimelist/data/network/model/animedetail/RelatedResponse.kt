package com.sanmidev.yetanotheranimelist.data.network.model.animedetail


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RelatedResponse(
    @Json(name = "Adaptation")
    val adaptation: List<AdaptationResponse>?,

    @Json(name = "Prequel")
    val prequelResponse: List<PrequelResponse>?,

    @Json(name="Sequel")
    val sequelResponse: List<SequelResponse>?

)