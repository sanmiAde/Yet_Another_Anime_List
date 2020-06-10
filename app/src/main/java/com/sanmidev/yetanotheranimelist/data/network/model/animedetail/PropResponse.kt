package com.sanmidev.yetanotheranimelist.data.network.model.animedetail


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PropResponse(
    @Json(name = "from")
    val from: FromResponse,
    @Json(name = "to")
    val to: ToResponse
)