package com.sanmidev.yetanotheranimelist.feature.upComingAnimes

import android.app.Application
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
import timber.log.Timber
import javax.inject.Inject

class UpComingAnimesViewModel(
    private val jikanRepository: JikanRepository,
    private val rxScheduler: RxScheduler,
    private val application: Application
) : ViewModel() {

    private val getUpComingAnimesMutableLiveData: MutableLiveData<AnimeListResult> =
        MutableLiveData<AnimeListResult>()


    private val getNextUpComingAnimesMutableLiveData: MutableLiveData<AnimeListResult> =
        MutableLiveData<AnimeListResult>()

    private val compositeDisposable = CompositeDisposable()

    val animeMutableListData = mutableListOf<AnimeEntity>()


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
        private val rxScheduler: RxScheduler,
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UpComingAnimesViewModel(jikanRepository, rxScheduler, application) as T
        }

    }

    fun getUpComingAnimes() {

        getUpComingAnimesMutableLiveData.value = AnimeListResult.Loading

        compositeDisposable.add(

            jikanRepository.getUpComingAnimeList(currentPage)
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

        currentPage += 1

        getNextUpComingAnimesMutableLiveData.value = AnimeListResult.Loading

        compositeDisposable.add(
            jikanRepository.getUpComingAnimeList(currentPage)
                .subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main())
                .subscribeBy(
                    onError = { throwable: Throwable ->

                        Timber.d(throwable.localizedMessage)

                        //If request is not successful, set currentPage to the previous page.
                        currentPage -= 1

                        getNextUpComingAnimesMutableLiveData.value =
                            AnimeListResult.Exception(throwable.localizedMessage, throwable)
                    },
                    onSuccess = { animeListResult: AnimeListResult ->
                        getNextUpComingAnimesMutableLiveData.value = animeListResult


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
        animeMutableListData.clear()
        animeMutableListData.addAll(list)
    }


    /***
     * This method add the next data gotten from the jikan list api.
     * @param list is the anime list gotten from the jikan api
     *
     */
    fun addNextData(list: List<AnimeEntity>) {
        animeMutableListData.addAll(list)
    }


    override fun onCleared() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.clear()
        }
        super.onCleared()
    }


}