package com.sanmidev.yetanotheranimelist.data.network.model

import com.sanmidev.yetanotheranimelist.data.local.model.AnimeEntityList

sealed class AnimeListResult {

    class loading() : AnimeListResult()

    class success(val data: AnimeEntityList) : AnimeListResult()

    class APIerror(val animeListErrorRespones: AnimeListErrorRespones) : AnimeListResult()

    class Exception(val message : String, val throwable: Throwable) : AnimeListResult()
}
