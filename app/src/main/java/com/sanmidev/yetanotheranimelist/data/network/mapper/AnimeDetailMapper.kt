package com.sanmidev.yetanotheranimelist.data.network.mapper

import com.sanmidev.yetanotheranimelist.data.local.model.animedetail.*
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.*
import javax.inject.Inject

class AnimeDetailMapper @Inject constructor() {

    fun tranformAnimeDetailToAnimeEntity(animeDetailResponse: AnimeDetailResponse): AnimeDetailEnitity {
        val airedEntity = transformAiredResponseToAiredEntity(animeDetailResponse.aired)
        val listOfGenres = transformGenreReponseToGenreEntity(animeDetailResponse.genreResponses)

        return AnimeDetailEnitity(
            airedEntity,
            animeDetailResponse.airing ?: false,
            animeDetailResponse.broadcast ?: "",
            animeDetailResponse.duration ?: "",
            animeDetailResponse.endingThemes ?: emptyList(),
            animeDetailResponse.episodes ?: 0,
            listOfGenres,
            animeDetailResponse.imageUrl ?: "",
            animeDetailResponse.malId ?: 0,
            animeDetailResponse.openingThemes ?: emptyList(),
            animeDetailResponse.premiered ?: "",
            animeDetailResponse.rating ?: "",
            animeDetailResponse.trailerUrl ?: "",
            animeDetailResponse.score ?: "",
            animeDetailResponse.status ?: "",
            animeDetailResponse.synopsis ?: "",
            animeDetailResponse.title ?: "",
            animeDetailResponse.type ?: "",
            animeDetailResponse.url ?: ""
        )
    }


    fun transformGenreReponseToGenreEntity(genreResponse: List<GenreResponse>): List<GenreEntity> {
        return genreResponse.map { genreResponse: GenreResponse ->
            GenreEntity(
                genreResponse.malId ?: 0,
                genreResponse.name ?: "",
                genreResponse.type ?: "",
                genreResponse.url ?: ""
            )
        }
    }

    fun transformAiredResponseToAiredEntity(airedResponse: AiredResponse): AiredEntity {
        val propEntity = tranformPropResponseToPropEntity(airedResponse.prop)
        return AiredEntity(
            airedResponse.from ?: "",
            propEntity,
            airedResponse.string ?: "",
            airedResponse.to ?: ""
        )
    }

    fun tranformPropResponseToPropEntity(propResponse: PropResponse): PropEntity {
        val toEntity = tranformToResponseToToEntity(propResponse.to)
        val fromEntity = tranformFromResponseToFromEntity(propResponse.from)

        return PropEntity(fromEntity, toEntity)
    }


    fun tranformToResponseToToEntity(toResponse: ToResponse): ToEntity {
        return ToEntity(toResponse.day ?: "", toResponse.month ?: "", toResponse.year ?: "")
    }


    fun tranformFromResponseToFromEntity(fromResponse: FromResponse): FromEntity {
        return FromEntity(fromResponse.day ?: "", fromResponse.month ?: "", fromResponse.year ?: "")
    }

}