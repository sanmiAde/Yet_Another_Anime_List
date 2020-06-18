package com.sanmidev.yetanotheranimelist.data.network.model.animedetail


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//TODO MAKE ALL THE FIELDS NULLABLE
@JsonClass(generateAdapter = true)
data class AnimeDetailResponse(
    @Json(name = "aired")
    val aired: AiredResponse,
    @Json(name = "airing")
    val airing: Boolean?,
    @Json(name = "background")
    val background: String?,
    @Json(name = "broadcast")
    val broadcast: String?,
    @Json(name = "duration")
    val duration: String?,
    @Json(name = "ending_themes")
    val endingThemes: List<String>?,
    @Json(name = "episodes")
    val episodes: Int?,
    @Json(name = "favorites")
    val favorites: Int?,
    @Json(name = "genres")
    val genreResponses: List<GenreResponse>,
    @Json(name = "image_url")
    val imageUrl: String?,
    @Json(name = "licensors")
    val licensors: List<Any>?,
    @Json(name = "mal_id")
    val malId: Int?,
    @Json(name = "members")
    val members: Int?,
    @Json(name = "opening_themes")
    val openingThemes: List<String>?,
    @Json(name = "popularity")
    val popularity: Int?,
    @Json(name = "premiered")
    val premiered: String?,
    @Json(name = "producers")
    val producers: List<Any>?,
    @Json(name = "rank")
    val rank: Any?,
    @Json(name = "rating")
    val rating: String?,
    @Json(name = "related")
    val relatedResponse: RelatedResponse?,
    @Json(name = "request_cache_expiry")
    val requestCacheExpiry: Int?,
    @Json(name = "request_cached")
    val requestCached: Boolean?,
    @Json(name = "request_hash")
    val requestHash: String?,
    @Json(name = "score")
    val score: String?,
    @Json(name = "scored_by")
    val scoredBy: Any?,
    @Json(name = "source")
    val source: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "studios")
    val studioResponses: List<StudioResponse>?,
    @Json(name = "synopsis")
    val synopsis: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "title_english")
    val titleEnglish: Any?,
    @Json(name = "title_japanese")
    val titleJapanese: String?,
    @Json(name = "title_synonyms")
    val titleSynonyms: List<String>?,
    @Json(name = "trailer_url")
    val trailerUrl: String?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "url")
    val url: String?
)