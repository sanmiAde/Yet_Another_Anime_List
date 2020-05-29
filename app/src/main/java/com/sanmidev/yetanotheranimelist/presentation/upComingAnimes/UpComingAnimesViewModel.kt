package com.sanmidev.yetanotheranimelist.presentation.upComingAnimes

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sanmidev.yetanotheranimelist.R
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeListResult
import com.sanmidev.yetanotheranimelist.data.network.repo.JikanRepository
import com.sanmidev.yetanotheranimelist.utils.RxScheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class UpComingAnimesViewModel(
    private val jikanRepository: JikanRepository,
    private val rxScheduler: RxScheduler,
    private val application: Application
) : ViewModel() {

    private val getUpComingAnimesMutableLiveData = MutableLiveData<AnimeListResult>()

    private var currentPage = 1

    private val compositeDisposable = CompositeDisposable()

    val upComingLiveData: LiveData<AnimeListResult>
        get() = getUpComingAnimesMutableLiveData

    class VMFactory @Inject constructor(
        private val jikanRepository: JikanRepository,
        private val rxScheduler: RxScheduler,
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UpComingAnimesViewModel(jikanRepository, rxScheduler, application ) as T
        }

    }

    fun getUpComingAnimes() {

        getUpComingAnimesMutableLiveData.value = AnimeListResult.loading()

        compositeDisposable.add(
            jikanRepository.getUpComingAnimeList(1)
                .subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main())
                .subscribeBy(
                    onError = { throwable: Throwable ->
                        getUpComingAnimesMutableLiveData.value =
                            AnimeListResult.Exception(application.getString(R.string.no_network_error), throwable)
                    },
                    onSuccess = { animeListResult: AnimeListResult ->
                        getUpComingAnimesMutableLiveData.value = animeListResult
                    }
                )
        )
    }


    fun cancelSubscriptions() {
        compositeDisposable.dispose()
    }


    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}