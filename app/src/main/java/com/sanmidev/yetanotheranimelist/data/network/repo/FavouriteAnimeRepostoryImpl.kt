package com.sanmidev.yetanotheranimelist.data.network.repo

import com.sanmidev.yetanotheranimelist.data.local.dao.FavouriteAnimeDao
import com.sanmidev.yetanotheranimelist.data.local.model.FavouriteAnimeResult
import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.AnimeDetailResult
import com.sanmidev.yetanotheranimelist.utils.RxScheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class FavouriteAnimeRepostoryImpl @Inject constructor(
    private val favouriteAnimeDao: FavouriteAnimeDao,
    private val rxScheduler: RxScheduler
) :

    FavouriteAnimeRepository {

    private val compositeDisposable = CompositeDisposable()


    override fun hasBeenSaved(malID: Int): Single<Boolean> {
        return favouriteAnimeDao.getAnime(malID)
            .map { it == 1 }
            .subscribeOn(rxScheduler.io())
            .observeOn(rxScheduler.main())
    }

    override fun getAnimeSize(): Int {
        return favouriteAnimeDao.getFavouriteAnimes()
            .observeOn(rxScheduler.main())
            .subscribeOn(rxScheduler.io()).blockingGet()
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


        return Single.create<FavouriteAnimeResult> {
            if (hasBeenSaved) {
                favouriteAnimeDao.unFavouriteAnime(animeEntity).subscribeBy(
                    onComplete = {
                        if (!it.isDisposed) {
                            it.onSuccess(FavouriteAnimeResult.unFavourited)
                        }
                    }, onError = { throwable ->
                        if (!it.isDisposed) {
                            it.onError(throwable)
                        }
                    }
                )

            } else {
                favouriteAnimeDao.favouriteAnime(animeEntity).subscribeBy(
                    onComplete = {
                        if (!it.isDisposed) {
                            it.onSuccess(FavouriteAnimeResult.favourited)
                        }
                    }, onError = { throwable ->
                        if (!it.isDisposed) {
                            it.onError(throwable)
                        }
                    })
            }
        }
    }


    fun clearDisposable() {
        if (compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}