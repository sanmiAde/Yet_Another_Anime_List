package com.sanmidev.yetanotheranimelist.data.network.mapper

import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntityList
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeListResponse
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeResponse
import javax.inject.Inject

/**
 * Mapper class for mapping [AnimeListResponse] to [AnimeEntityList]
 */

class AnimeListMapper @Inject constructor() {

    /***
     * Transforms [AnimeListResponse] to [AnimeEntityList]
     * @param animeListResponse from the server
     * @return AnimeEnityList is the transformed object.
     */
    fun transformAnimeListToEntity(animeListResponse: AnimeListResponse) : AnimeEntityList {
        val animeEntities = mutableListOf<AnimeEntity>()
         animeListResponse.animeResponses.map { animeResponse: AnimeResponse ->

           animeEntities.add(
               AnimeEntity(
                   animeResponse.endDate ?: "",
                   animeResponse.episodes ?: 0,
                   animeResponse.imageUrl,
                   animeResponse.malId,
                   animeResponse.score,
                   animeResponse.startDate ?: "",
                   animeResponse.title,
                   animeResponse.type,
                   animeResponse.url
               )
           )
        }

        return AnimeEntityList(
            animeEntities
        )
    }


}