package com.sanmidev.yetanotheranimelist.data.local.model.favourite

import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntity

sealed class FavouriteAnimeListResult {

    object Loading : FavouriteAnimeListResult()

    class Loaded(val favAnimeList: List<AnimeEntity>) : FavouriteAnimeListResult()

    class Error(val message: String, val throwable: Throwable) : FavouriteAnimeListResult()
}