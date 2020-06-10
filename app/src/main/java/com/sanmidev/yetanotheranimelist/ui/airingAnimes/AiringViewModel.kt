package com.sanmidev.yetanotheranimelist.ui.airingAnimes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

    private val compositeDisposable = CompositeDisposable()

    private var _currentPage = 1

    val currentPage: Int
        get() = _currentPage

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
        _currentPage += 1

        nextAiringAnimeMutableLiveData.value = AnimeListResult.Loading

        compositeDisposable.add(
            jikanRepository.getAiringAnimes(currentPage).subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main()).subscribeBy(
                    onSuccess = { animeListResult: AnimeListResult ->
                        nextAiringAnimeMutableLiveData.value = animeListResult
                    },
                    onError = { throwable: Throwable ->
                        _currentPage -= 1
                        nextAiringAnimeMutableLiveData.value =
                            AnimeListResult.Exception("Could Not Connect To Server", throwable)
                    }

                )
        )
    }


    fun cancelSubscription() {
        compositeDisposable.dispose()
    }

    override fun onCleared() {
        cancelSubscription()
        super.onCleared()

    }


}
