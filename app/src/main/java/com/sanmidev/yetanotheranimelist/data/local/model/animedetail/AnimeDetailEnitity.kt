package com.sanmidev.yetanotheranimelist.data.local.model.animedetail


data class AnimeDetailEnitity(
    val aired: AiredEntity,
    val airing: Boolean,
    val broadcast: String,
    val duration: String,
    val endingThemes: List<String>,
    val episodes: Int?,
    val genreEntity: List<GenreEntity>,
    val imageUrl: String,
    val id: Int,
    val openingThemes: List<String>,
    val premiered: String,
    val rating: String,
    val trailerUrl: String,
    val score: String?,
    val status: String,
    val synopsis: String,
    val title: String,
    val type: String,
    val url: String

)


