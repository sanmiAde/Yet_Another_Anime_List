package com.sanmidev.yetanotheranimelist.data.network.repo

import com.sanmidev.yetanotheranimelist.data.local.dao.FavouriteAnimeDao
import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.local.model.favourite.FavouriteAnimeResult
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.AnimeDetailResult
import com.sanmidev.yetanotheranimelist.utils.RxScheduler
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class FavouriteAnimeRepostoryImpl @Inject constructor(
    private val favouriteAnimeDao: FavouriteAnimeDao,
    private val rxScheduler: RxScheduler
) : FavouriteAnimeRepository {


    override fun hasBeenSaved(malID: Int): Single<Boolean> {
        return favouriteAnimeDao.getAnime(malID)
            .map { it == 1 }
            .subscribeOn(rxScheduler.io())
            .observeOn(rxScheduler.main())
    }

    /***
     *
     */
    override fun favouriteAnime(
        animeResult: AnimeDetailResult,
        hasBeenSaved: Boolean
    ): Single<out FavouriteAnimeResult> {

        val successResult = animeResult as AnimeDetailResult.Success

        val animeEntity = AnimeEntity(
            successResult.data.imageUrl,
            successResult.data.id,
            successResult.data.title
        )

        return when {
            hasBeenSaved -> {
                favouriteAnimeDao.unFavouriteAnime(animeEntity)
                    .toSingleDefault(FavouriteAnimeResult.UnFavourited)

            }
            else -> {
                favouriteAnimeDao.favouriteAnime(animeEntity)
                    .toSingleDefault(FavouriteAnimeResult.Favourited)
            }
        }
    }


    override fun getAnimeList(): Observable<List<AnimeEntity>> {
        return favouriteAnimeDao.getAnimeList()

    }


}