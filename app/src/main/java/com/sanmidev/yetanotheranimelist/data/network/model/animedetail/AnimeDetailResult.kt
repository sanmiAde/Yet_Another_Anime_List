package com.sanmidev.yetanotheranimelist.data.network.model.animedetail

import com.sanmidev.yetanotheranimelist.data.local.model.animedetail.AnimeDetailEnitity
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeListResult
import com.sanmidev.yetanotheranimelist.data.network.model.error.JikanErrorRespone

sealed class AnimeDetailResult{

    object Loading : AnimeDetailResult()

    class Success(val data : AnimeDetailEnitity) : AnimeDetailResult()

    class APIerror(val jikanErrorRespone: JikanErrorRespone) : AnimeDetailResult()

    class Exception(val message : String, val throwable: Throwable) : AnimeDetailResult()

}