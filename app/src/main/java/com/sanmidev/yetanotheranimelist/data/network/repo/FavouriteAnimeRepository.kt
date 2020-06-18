package com.sanmidev.yetanotheranimelist.data.network.repo

import com.sanmidev.yetanotheranimelist.data.local.model.FavouriteAnimeResult
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.AnimeDetailResult
import io.reactivex.Completable
import io.reactivex.Single

interface FavouriteAnimeRepository {


    fun hasBeenSaved(malID: Int): Single<Boolean>

    fun getAnimeSize() : Int
    fun favouriteAnime(
        animeResult: AnimeDetailResult,
        hasBeenSaved: Boolean
    ): Single<out FavouriteAnimeResult>
}