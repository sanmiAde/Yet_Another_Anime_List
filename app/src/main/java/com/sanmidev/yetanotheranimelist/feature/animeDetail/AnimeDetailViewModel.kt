package com.sanmidev.yetanotheranimelist.feature.animeDetail

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.AnimeDetailResult
import com.sanmidev.yetanotheranimelist.data.network.repo.JikanRepository
import com.sanmidev.yetanotheranimelist.utils.RxScheduler
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class AnimeDetailViewModel(
    private val jikanRepository: JikanRepository,
    private val rxScheduler: RxScheduler,
    private val application: Application,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val animeDetailMutableResult = MutableLiveData<AnimeDetailResult>()


    val compositeDisposable = CompositeDisposable()

    val animeDetailResultState: LiveData<AnimeDetailResult>
        get() = animeDetailMutableResult


    init {
        getAnimeDetail()
    }

    class VmFactory @AssistedInject constructor(
        private val jikanRepository: JikanRepository,
        private val rxScheduler: RxScheduler,
        private val application: Application,
        @Assisted savedStateRegistryOwner: SavedStateRegistryOwner,
        @Assisted defaultArgs: Bundle
    ) : AbstractSavedStateViewModelFactory(savedStateRegistryOwner, defaultArgs) {
        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            return AnimeDetailViewModel(jikanRepository, rxScheduler, application, handle) as T
        }


        @AssistedInject.Factory
        interface Factory {
            fun createFactory(
                savedStateRegistryOwner: SavedStateRegistryOwner,
                defaultArgs: Bundle
            ): VmFactory
        }
    }

    private fun getAnimeDetail() {
        val malId = savedStateHandle.get<Int>(AnimeDetailFragment.DETAIL_ANIME_ID_KEY) ?: 0

        compositeDisposable.add(
            jikanRepository.getAnimeDetail(malId)
                .subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main())
                .subscribeBy(
                    onError = {
                        animeDetailMutableResult.value =
                            AnimeDetailResult.Exception("Could not connect to server", it)
                    },
                    onSuccess = { animeDetailResult: AnimeDetailResult ->

                        animeDetailMutableResult.value = animeDetailResult
                    }
                )
        )
    }


}
