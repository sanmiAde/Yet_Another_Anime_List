package com.sanmidev.yetanotheranimelist.feature.animeDetail

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.sanmidev.yetanotheranimelist.data.local.model.animedetail.GenreEntity
import com.sanmidev.yetanotheranimelist.data.local.model.favourite.FavouriteAnimeResult
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.AnimeDetailResult
import com.sanmidev.yetanotheranimelist.data.network.repo.FavouriteAnimeRepository
import com.sanmidev.yetanotheranimelist.data.network.repo.JikanRepository
import com.sanmidev.yetanotheranimelist.utils.RxScheduler
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class AnimeDetailViewModel(
    private val jikanRepository: JikanRepository,
    private val favouriteAnimeRepostoryImpl: FavouriteAnimeRepository,
    private val rxScheduler: RxScheduler,
    private val application: Application,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val animeDetailMutableResult = MutableLiveData<AnimeDetailResult>()

    private val isFavouritedMutableLiveData = MutableLiveData<FavouriteAnimeResult>()

    val compositeDisposable = CompositeDisposable()

    val animeDetailResultState: LiveData<AnimeDetailResult>
        get() = animeDetailMutableResult

    val isFavourited: LiveData<FavouriteAnimeResult>
        get() = isFavouritedMutableLiveData

    private var id = Int.MIN_VALUE

    private lateinit var animeResult: AnimeDetailResult


    init {
        getAnimeDetail()
    }

    class VmFactory @AssistedInject constructor(
        private val jikanRepository: JikanRepository,
        private val favouriteAnimeRepostoryImpl: FavouriteAnimeRepository,
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
            return AnimeDetailViewModel(
                jikanRepository,
                favouriteAnimeRepostoryImpl,
                rxScheduler,
                application,
                handle
            ) as T
        }


        @AssistedInject.Factory
        interface Factory {
            fun createFactory(
                savedStateRegistryOwner: SavedStateRegistryOwner,
                defaultArgs: Bundle
            ): VmFactory
        }
    }

    /****
     * Gets the anime detail from the jikan api
     * malId is the id of the anime passed from the [AnimeDetailFragment] to the [AnimeDetailViewModel] via savedStateHandle.
     */
    private fun getAnimeDetail() {
        id = savedStateHandle.get<Int>(AnimeDetailFragment.DETAIL_ANIME_ID_KEY) ?: 0

        animeDetailMutableResult.value = AnimeDetailResult.Loading

        compositeDisposable.add(
            jikanRepository.getAnimeDetail(id)
                .subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main())
                .subscribeBy(
                    onError = {
                        Timber.d(it.toString())
                        animeDetailMutableResult.value =
                            AnimeDetailResult.Exception("Could not connect to server", it)
                    },
                    onSuccess = { animeDetailResult: AnimeDetailResult ->

                        animeResult = animeDetailResult
                        animeDetailMutableResult.value = animeDetailResult
                    }
                )
        )
    }


    fun favouriteAnime() {

        favouriteAnimeRepostoryImpl.hasBeenSaved(id).flatMap {
            favouriteAnimeRepostoryImpl.favouriteAnime(animeResult, it)
                .subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main())
        }.subscribeBy(
                onSuccess = {
                    isFavouritedMutableLiveData.value = it
                },
                onError = {

                }
            ).addTo(compositeDisposable)


    }

    fun hasBeenFavourited() {
        favouriteAnimeRepostoryImpl.hasBeenSaved(id).subscribeBy({

        }, { hasBeenSaved ->
            if (hasBeenSaved) {
                isFavouritedMutableLiveData.value = FavouriteAnimeResult.Favourited
            } else {
                isFavouritedMutableLiveData.value = FavouriteAnimeResult.UnFavourited
            }
        }).addTo(compositeDisposable)
    }


    override fun onCleared() {
        disposeCompositeDisposable()
        super.onCleared()
    }

    fun disposeCompositeDisposable() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }


    fun processGenre(genreEntities: List<GenreEntity>): List<String> {
        return genreEntities.map { it.name }
    }

    companion object {
        const val ANIME_DETAIL_KEY = "com.sanmidev.yetanotheranimelist.anime_detail_key"
    }

}
