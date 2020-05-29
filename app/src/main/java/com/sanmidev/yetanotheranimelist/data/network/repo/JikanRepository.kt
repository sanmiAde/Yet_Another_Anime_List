package com.sanmidev.yetanotheranimelist.data.network.repo

import com.sanmidev.yetanotheranimelist.data.local.model.AnimeEntityList
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeListResult
import io.reactivex.Single

interface JikanRepository {
    fun getUpComingAnimeList(page : Int): Single<AnimeListResult>

}