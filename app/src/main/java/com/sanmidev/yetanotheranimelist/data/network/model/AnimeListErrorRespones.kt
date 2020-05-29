package com.sanmidev.yetanotheranimelist.data.network.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimeListErrorRespones(
    @Json(name = "error")
    val error: String,
    @Json(name = "message")
    val message: String,
    @Json(name = "status")
    val status: Int,
    @Json(name = "type")
    val type: String
)