package com.sanmidev.yetanotheranimelist.data.local.model

sealed class FavouriteAnimeResult {

    object favourited : FavouriteAnimeResult()

    object unFavourited : FavouriteAnimeResult()

    class error(val errorMessages: String, throwable: Throwable) : FavouriteAnimeResult()
}