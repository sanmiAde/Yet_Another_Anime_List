package com.sanmidev.yetanotheranimelist.data.local.repo

import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.local.model.favourite.FavouriteAnimeResult
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.AnimeDetailResult
import io.reactivex.Observable
import io.reactivex.Single

class FakeFavouriteAnimeRepository :
    FavouriteAnimeRepository {


    override fun hasBeenSaved(malID: Int): Single<Boolean> {
        return Single.just(true)
    }


    override fun favouriteAnime(
        animeResult: AnimeDetailResult,
        hasBeenSaved: Boolean
    ): Single<out FavouriteAnimeResult> {
        return when {
            hasBeenSaved -> {
                Single.just(FavouriteAnimeResult.UnFavourited)
            }
            else -> {
                Single.just(FavouriteAnimeResult.Favourited)
            }

        }

    }

    override fun getAnimeList(): Observable<List<AnimeEntity>> {
        return Observable.just(emptyList())
    }
}