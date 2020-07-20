package com.sanmidev.yetanotheranimelist.feature.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sanmidev.yetanotheranimelist.data.local.model.favourite.FavouriteAnimeListResult
import com.sanmidev.yetanotheranimelist.data.local.repo.FavouriteAnimeRepository
import com.sanmidev.yetanotheranimelist.utils.AppScheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class FavouriteViewModel(
    private val favouriteAnimeRepository: FavouriteAnimeRepository,
    private val appScheduler: AppScheduler
) : ViewModel() {


    class VMFactory @Inject constructor(
        private val favouriteAnimeRepository: FavouriteAnimeRepository,
        private val appScheduler: AppScheduler
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FavouriteViewModel(favouriteAnimeRepository, appScheduler) as T
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

        favouriteAnimeRepository.getAnimeList()
            .subscribeOn(appScheduler.io())
            .observeOn(appScheduler.main())
            .doOnSubscribe {
                animeListMutableLiveData.value = FavouriteAnimeListResult.Loading
            }
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
