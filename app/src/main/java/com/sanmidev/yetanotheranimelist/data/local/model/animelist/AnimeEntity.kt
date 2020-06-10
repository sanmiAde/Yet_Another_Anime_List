package com.sanmidev.yetanotheranimelist.data.local.model.animelist

data class AnimeEntity(
    val endDate: String,
    val episodes: Int,
    val imageUrl: String,
    val id: Int,
    val score: Double,
    val startDate: String,
    val title: String,
    val type: String,
    val animeUrl: String

)
