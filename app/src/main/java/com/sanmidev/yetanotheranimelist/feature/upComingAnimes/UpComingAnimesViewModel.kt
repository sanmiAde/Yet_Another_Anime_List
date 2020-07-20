package com.sanmidev.yetanotheranimelist.feature.upComingAnimes

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeListResult
import com.sanmidev.yetanotheranimelist.data.network.repo.JikanRepository
import com.sanmidev.yetanotheranimelist.utils.AppScheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class UpComingAnimesViewModel(
    private val jikanRepository: JikanRepository,
    private val rxScheduler: AppScheduler,
    private val application: Application
) : ViewModel() {

    private val getUpComingAnimesMutableLiveData: MutableLiveData<AnimeListResult> =
        MutableLiveData<AnimeListResult>()

    private val getNextUpComingAnimesMutableLiveData: MutableLiveData<AnimeListResult> =
        MutableLiveData<AnimeListResult>()

    private val compositeDisposable = CompositeDisposable()

    private val animeMutableListData = mutableListOf<AnimeEntity>()

    val animeListData: List<AnimeEntity>
        get() = animeMutableListData


    var currentPage: Int = 1
        private set

    val nextUpComingLiveData: LiveData<AnimeListResult>
        get() = getNextUpComingAnimesMutableLiveData

    val upComingLiveData: LiveData<AnimeListResult>
        get() = getUpComingAnimesMutableLiveData


    var error = ""


    init {
        getUpComingAnimes()
    }


    class VMFactory @Inject constructor(
        private val jikanRepository: JikanRepository,
        private val appScheduler: AppScheduler,
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UpComingAnimesViewModel(jikanRepository, appScheduler, application) as T
        }

    }

    private fun getUpComingAnimes() {
        compositeDisposable.add(
            jikanRepository.getUpComingAnimeList(currentPage)
                .subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main())
                .doOnSubscribe {
                    getUpComingAnimesMutableLiveData.value = AnimeListResult.Loading
                }
                .subscribeBy(
                    onError = { throwable: Throwable ->

                        error = throwable.localizedMessage
                        getUpComingAnimesMutableLiveData.value =
                            AnimeListResult.Exception("Could Not Connect To Server", throwable)
                    },
                    onSuccess = { animeListResult: AnimeListResult ->
                        if (animeListResult is AnimeListResult.Success) {
                            animeMutableListData.addAll(animeListResult.data.animeEnities)
                        }

                        getUpComingAnimesMutableLiveData.value = animeListResult
                    }
                )
        )
    }

    fun getNextUpComingAnimes() {

        currentPage += 1

        compositeDisposable.add(
            jikanRepository.getUpComingAnimeList(currentPage)
                .subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main())
                .doOnSubscribe {
                    getNextUpComingAnimesMutableLiveData.value = AnimeListResult.Loading
                }
                .subscribeBy(
                    onError = { throwable: Throwable ->

                        //If request is not successful, set currentPage to the previous page.
                        currentPage -= 1

                        getNextUpComingAnimesMutableLiveData.value =
                            AnimeListResult.Exception(throwable.localizedMessage, throwable)
                    },
                    onSuccess = { animeListResult: AnimeListResult ->
                        if (animeListResult is AnimeListResult.Success) {
                            animeMutableListData.addAll(animeListResult.data.animeEnities)
                        }
                        getNextUpComingAnimesMutableLiveData.value = animeListResult

                    }
                )
        )
    }



    override fun onCleared() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.clear()
        }
        super.onCleared()
    }


}