package com.sanmidev.yetanotheranimelist.data.network.model.animelist

import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntityList
import com.sanmidev.yetanotheranimelist.data.network.model.error.JikanErrorRespone

sealed class AnimeListResult {

    object Loading : AnimeListResult()

    class Success(val data: AnimeEntityList) : AnimeListResult()

    class APIerror(val jikanErrorRespone: JikanErrorRespone) : AnimeListResult()

    class Exception(val message : String, val throwable: Throwable) : AnimeListResult()
}



