package com.sanmidev.yetanotheranimelist.feature.airingAnimes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeListResult
import com.sanmidev.yetanotheranimelist.data.network.repo.JikanRepository
import com.sanmidev.yetanotheranimelist.utils.RxScheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class AiringViewModel(
    private val jikanRepository: JikanRepository,
    private val rxScheduler: RxScheduler
) : ViewModel() {

    private val airingAnimeMutableLiveData = MutableLiveData<AnimeListResult>()

    private val nextAiringAnimeMutableLiveData = MutableLiveData<AnimeListResult>()

    val animeListData = mutableListOf<AnimeEntity>()

    private val compositeDisposable = CompositeDisposable()

    var currentPage: Int = 1
        private set

    val airingLiveData: LiveData<AnimeListResult>
        get() = airingAnimeMutableLiveData

    val nextAiringLiveData: LiveData<AnimeListResult>
        get() = nextAiringAnimeMutableLiveData

    class VMFactory @Inject constructor(
        private val jikanRepository: JikanRepository,
        private val rxScheduler: RxScheduler
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AiringViewModel(jikanRepository, rxScheduler) as T
        }
    }


    init {
        getAiringAnimes()
    }

    private fun getAiringAnimes() {
        airingAnimeMutableLiveData.value = AnimeListResult.Loading


        compositeDisposable.add(
            jikanRepository.getAiringAnimes(currentPage).subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main()).subscribeBy(
                    onSuccess = { animeListResult: AnimeListResult ->
                        airingAnimeMutableLiveData.value = animeListResult
                    },
                    onError = { throwable: Throwable ->
                        airingAnimeMutableLiveData.value =
                            AnimeListResult.Exception("Could Not Connect To Server", throwable)
                    }

                )
        )
    }

    fun getNextAiringAnime() {
        currentPage += 1

        nextAiringAnimeMutableLiveData.value = AnimeListResult.Loading

        compositeDisposable.add(
            jikanRepository.getAiringAnimes(currentPage).subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main()).subscribeBy(
                    onSuccess = { animeListResult: AnimeListResult ->
                        nextAiringAnimeMutableLiveData.value = animeListResult
                    },
                    onError = { throwable: Throwable ->
                        currentPage -= 1
                        nextAiringAnimeMutableLiveData.value =
                            AnimeListResult.Exception("Could Not Connect To Server", throwable)
                    }

                )
        )
    }

    /***
     * This method add the intitial data gotten from the jikan list api.
     * @param list is the anime list gotten from the jikan api
     *
     */
    fun addInitialDataToList(list: List<AnimeEntity>) {
        animeListData.clear()
        animeListData.addAll(list)
    }


    /***
     * This method add the next data gotten from the jikan list api.
     * @param list is the anime list gotten from the jikan api
     *
     */
    fun addNextData(list: List<AnimeEntity>) {
        animeListData.addAll(list)
    }

    override fun onCleared() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.clear()
        }
        super.onCleared()

    }


}
