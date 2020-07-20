package com.sanmidev.yetanotheranimelist.data.local.repo

import com.sanmidev.yetanotheranimelist.data.local.dao.FavouriteAnimeDao
import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.local.model.favourite.FavouriteAnimeResult
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.AnimeDetailResult
import com.sanmidev.yetanotheranimelist.utils.AppScheduler
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class FavouriteAnimeRepostoryImpl @Inject constructor(
    private val favouriteAnimeDao: FavouriteAnimeDao,
    private val appScheduler: AppScheduler
) : FavouriteAnimeRepository {


    /****
     * This method checks if an anime has been previously saved.
     * @param malID is the id of [AnimeEntity]
     * @return Single observable containing the saved status of the anime
     */
    override fun hasBeenSaved(malID: Int): Single<Boolean> {
        return favouriteAnimeDao.getAnime(malID)
            .map { it == 1 }
            .subscribeOn(appScheduler.io())
            .observeOn(appScheduler.main())
    }

    /***
     *This method favourites an anime.
     * if the anime has been favourited before, it is unfavourited and vise versa.
     * @param animeResult is the [AnimeDetailResult] from the server.
     * @param hasBeenSaved is the current favourite status of the anime.
     * @return Single observable containing the current favourite state status.
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