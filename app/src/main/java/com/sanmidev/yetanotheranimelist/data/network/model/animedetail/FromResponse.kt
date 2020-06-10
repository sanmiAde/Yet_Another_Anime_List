package com.sanmidev.yetanotheranimelist.data.network.model.animedetail


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FromResponse(
    @Json(name = "day")
    val day: String,
    @Json(name = "month")
    val month: String,
    @Json(name = "year")
    val year: String
)