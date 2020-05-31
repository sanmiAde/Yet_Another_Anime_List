package com.sanmidev.yetanotheranimelist.ui.upComingAnimes

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeListResult
import com.sanmidev.yetanotheranimelist.data.network.repo.JikanRepository
import com.sanmidev.yetanotheranimelist.utils.RxScheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

class UpComingAnimesViewModel(
    private val jikanRepository: JikanRepository,
    private val rxScheduler: RxScheduler,
    private val application: Application
) : ViewModel() {

    private var getUpComingAnimesMutableLiveData: MutableLiveData<AnimeListResult> =
        MutableLiveData<AnimeListResult>()


    private var getNextUpComingAnimesMutableLiveData: MutableLiveData<AnimeListResult> =
        MutableLiveData<AnimeListResult>()

    private val compositeDisposable = CompositeDisposable()

    private var _currentPage = 1

    val currentPage: Int
        get() = _currentPage

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
        private val rxScheduler: RxScheduler,
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UpComingAnimesViewModel(jikanRepository, rxScheduler, application) as T
        }

    }

    fun getUpComingAnimes() {

        getUpComingAnimesMutableLiveData.value = AnimeListResult.loading()

        compositeDisposable.add(

            jikanRepository.getUpComingAnimeList(_currentPage)
                .subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main())
                .subscribeBy(
                    onError = { throwable: Throwable ->

                        error = throwable.localizedMessage
                        getUpComingAnimesMutableLiveData.value =
                            AnimeListResult.Exception("Could Not Connect To Server", throwable)
                    },
                    onSuccess = { animeListResult: AnimeListResult ->

                        getUpComingAnimesMutableLiveData.value = animeListResult
                    }
                )
        )
    }

    fun getNextUpComingAnimes() {

        _currentPage += 1

        getNextUpComingAnimesMutableLiveData.value = AnimeListResult.loading()

        compositeDisposable.add(
            jikanRepository.getUpComingAnimeList(_currentPage)
                .subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main())
                .subscribeBy(
                    onError = { throwable: Throwable ->

                        Timber.d(throwable.localizedMessage)

                        //If request is not successful, set currentPage to the previous page.
                        _currentPage -= 1

                        getNextUpComingAnimesMutableLiveData.value =
                            AnimeListResult.Exception(throwable.localizedMessage, throwable)
                    },
                    onSuccess = { animeListResult: AnimeListResult ->
                        getNextUpComingAnimesMutableLiveData.value = animeListResult


                    }
                )
        )
    }


    fun cancelSubscriptions() {
        compositeDisposable.dispose()
    }


    override fun onCleared() {
     cancelSubscriptions()
        super.onCleared()
    }


}