package com.sanmidev.yetanotheranimelist.data.network.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimeResponse(
    @Json(name = "end_date")
    val endDate: String?,
    @Json(name = "episodes")
    val episodes: Int?,
    @Json(name = "image_url")
    val imageUrl: String,
    @Json(name = "mal_id")
    val malId: Int,
    @Json(name = "members")
    val members: Int,
    @Json(name = "rank")
    val rank: Int,
    @Json(name = "score")
    val score: Double,
    @Json(name = "start_date")
    val startDate: String?,
    @Json(name = "title")
    val title: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "url")
    val url: String
)