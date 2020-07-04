package com.sanmidev.yetanotheranimelist.data.network.repo

import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.AnimeDetailResult
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeListResult
import io.reactivex.Single

/***
 * Interface for interating with the jikan api
 */
interface JikanRepository {
    fun getUpComingAnimeList(page : Int): Single<AnimeListResult>
    fun getAiringAnimes(page: Int): Single<AnimeListResult>
    fun getAnimeDetail(malId: Int): Single<AnimeDetailResult>
}