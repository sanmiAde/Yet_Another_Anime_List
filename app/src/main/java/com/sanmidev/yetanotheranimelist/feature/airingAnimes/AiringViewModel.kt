package com.sanmidev.yetanotheranimelist.feature.airingAnimes

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

class AiringViewModel(
    private val jikanRepository: JikanRepository,
    private val appScheduler: AppScheduler
) : ViewModel() {

    private val airingAnimeMutableLiveData = MutableLiveData<AnimeListResult>()

    private val nextAiringAnimeMutableLiveData = MutableLiveData<AnimeListResult>()

    private val compositeDisposable = CompositeDisposable()

    val animeListData = mutableListOf<AnimeEntity>()

    var currentPage: Int = 1
        private set

    val airingLiveData: LiveData<AnimeListResult>
        get() = airingAnimeMutableLiveData

    val nextAiringLiveData: LiveData<AnimeListResult>
        get() = nextAiringAnimeMutableLiveData

    class VMFactory @Inject constructor(
        private val jikanRepository: JikanRepository,
        private val appScheduler: AppScheduler
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AiringViewModel(jikanRepository, appScheduler) as T
        }
    }


    init {
        getAiringAnimes()
    }

    private fun getAiringAnimes() {

        compositeDisposable.add(
            jikanRepository.getAiringAnimes(currentPage)
                .subscribeOn(appScheduler.io())
                .observeOn(appScheduler.main())
                .doOnSubscribe {
                    airingAnimeMutableLiveData.value = AnimeListResult.Loading
                }
                .subscribeBy(
                    onSuccess = { animeListResult: AnimeListResult ->

                        if (animeListResult is AnimeListResult.Success) {
                            animeListData.addAll(animeListResult.data.animeEnities)
                        }

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

        compositeDisposable.add(
            jikanRepository.getAiringAnimes(currentPage)
                .subscribeOn(appScheduler.io())
                .observeOn(appScheduler.main())
                .doOnSubscribe {
                    nextAiringAnimeMutableLiveData.value = AnimeListResult.Loading
                }
                .subscribeBy(
                    onSuccess = { animeListResult: AnimeListResult ->
                        if (animeListResult is AnimeListResult.Success) {
                            animeListData.addAll(animeListResult.data.animeEnities)
                        }
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



    override fun onCleared() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.clear()
        }
        super.onCleared()

    }


}
