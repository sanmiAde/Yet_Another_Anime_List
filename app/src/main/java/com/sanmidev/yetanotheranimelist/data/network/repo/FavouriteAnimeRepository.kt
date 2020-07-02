package com.sanmidev.yetanotheranimelist.data.network.repo

import com.sanmidev.yetanotheranimelist.data.local.model.FavouriteAnimeResult
import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.AnimeDetailResult
import io.reactivex.Observable
import io.reactivex.Single

interface FavouriteAnimeRepository {


    fun hasBeenSaved(malID: Int): Single<Boolean>


    fun favouriteAnime(
        animeResult: AnimeDetailResult,
        hasBeenSaved: Boolean
    ): Single<out FavouriteAnimeResult>

    fun getAnimeList() : Observable<List<AnimeEntity>>
}