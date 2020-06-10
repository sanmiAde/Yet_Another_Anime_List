package com.sanmidev.yetanotheranimelist.data.network.model.animedetail


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AiredResponse(
    @Json(name = "from")
    val from: String,
    @Json(name = "prop")
    val prop: PropResponse,
    @Json(name = "string")
    val string: String,
    @Json(name = "to")
    val to: String
)