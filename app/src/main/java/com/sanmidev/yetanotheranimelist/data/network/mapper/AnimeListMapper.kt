package com.sanmidev.yetanotheranimelist.data.network.mapper

import com.sanmidev.yetanotheranimelist.data.local.model.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.local.model.AnimeEntityList
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeListResponse
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeResponse
import javax.inject.Inject

class AnimeListMapper @Inject constructor() {
    fun transformAnimeListToEntity(animeListResponse: AnimeListResponse) : AnimeEntityList {
        val animeEntities = mutableListOf<AnimeEntity>()
         animeListResponse.animeResponses.map { animeResponse: AnimeResponse ->
           animeEntities.add(AnimeEntity( animeResponse.endDate ?: "", animeResponse.episodes ?: 0, animeResponse.imageUrl, animeResponse.malId, animeResponse.score, animeResponse.startDate ?: "", animeResponse.title, animeResponse.type, animeResponse.url))
        }

        return  AnimeEntityList(animeEntities)
    }


}