package com.sanmidev.yetanotheranimelist.data.network.repo

import com.sanmidev.yetanotheranimelist.data.local.model.FavouriteAnimeResult
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.AnimeDetailResult
import io.reactivex.Single

class FakeFavouriteAnimeRepository : FavouriteAnimeRepository {


    override fun hasBeenSaved(malID: Int): Single<Boolean> {
        return Single.just(true)
    }

    override fun getAnimeSize(): Int {
        return 0
    }

    override fun favouriteAnime(
        animeResult: AnimeDetailResult,
        hasBeenSaved: Boolean
    ): Single<out FavouriteAnimeResult> {
        return Single.just(FavouriteAnimeResult.unFavourited)

    }
}