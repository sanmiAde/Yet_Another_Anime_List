package com.sanmidev.yetanotheranimelist.data.network.repo

import com.sanmidev.yetanotheranimelist.data.local.model.AnimeEntityList
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeListResult
import io.reactivex.Single

/***
 * Interface for interating with the jikan api
 */
interface JikanRepository {
    fun getUpComingAnimeList(page : Int): Single<AnimeListResult>
    fun getAiringAnimes(page: Int): Single<AnimeListResult>


}