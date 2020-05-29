package com.sanmidev.yetanotheranimelist.data.network.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimeListResponse(
    @Json(name = "request_cache_expiry")
    val requestCacheExpiry: Int,
    @Json(name = "request_cached")
    val requestCached: Boolean,
    @Json(name = "request_hash")
    val requestHash: String,
    @Json(name = "top")
    val animeResponses: List<AnimeResponse>
)