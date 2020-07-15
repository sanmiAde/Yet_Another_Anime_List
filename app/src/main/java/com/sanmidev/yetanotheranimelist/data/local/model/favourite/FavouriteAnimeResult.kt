package com.sanmidev.yetanotheranimelist.data.local.model.favourite

sealed class FavouriteAnimeResult {

    object Favourited : FavouriteAnimeResult()

    object UnFavourited : FavouriteAnimeResult()

    class Error(val errorMessages: String, throwable: Throwable) : FavouriteAnimeResult()
}