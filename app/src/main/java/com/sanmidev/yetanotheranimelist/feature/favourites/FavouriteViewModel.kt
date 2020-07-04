package com.sanmidev.yetanotheranimelist.feature.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sanmidev.yetanotheranimelist.data.local.model.favourite.FavouriteAnimeListResult
import com.sanmidev.yetanotheranimelist.data.network.repo.FavouriteAnimeRepository
import com.sanmidev.yetanotheranimelist.utils.RxScheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class FavouriteViewModel(
    private val favouriteAnimeRepository: FavouriteAnimeRepository,
    private val rxScheduler: RxScheduler
) : ViewModel() {


    class VMFactory @Inject constructor(
        private val favouriteAnimeRepository: FavouriteAnimeRepository,
        private val rxScheduler: RxScheduler
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FavouriteViewModel(favouriteAnimeRepository, rxScheduler) as T
        }
    }

    private val compositeDisposable = CompositeDisposable()

    private val animeListMutableLiveData = MutableLiveData<FavouriteAnimeListResult>()

    val animeLiveData: LiveData<FavouriteAnimeListResult>
        get() = animeListMutableLiveData

    init {
        getFavouriteAnimes()
    }

    private fun getFavouriteAnimes() {

        animeListMutableLiveData.value = FavouriteAnimeListResult.Loading

        favouriteAnimeRepository.getAnimeList()
            .subscribeOn(rxScheduler.io())
            .observeOn(rxScheduler.main())
            .subscribeBy(
                onComplete = {

                },
                onError = { throwable ->
                    animeListMutableLiveData.value =
                        FavouriteAnimeListResult.Error("Could get favourite animes", throwable)
                },
                onNext = { animeList ->
                    animeListMutableLiveData.postValue(FavouriteAnimeListResult.Loaded(animeList))
                }
            ).addTo(compositeDisposable)
    }

}
