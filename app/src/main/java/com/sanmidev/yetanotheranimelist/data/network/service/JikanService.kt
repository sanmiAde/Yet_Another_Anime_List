package com.sanmidev.yetanotheranimelist.data.network.service

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface JikanService {

    @GET("/v3/top/anime/{page}/upcoming")
    fun getUpandComingAnime(@Path("page") page : String) : Single<Response<ResponseBody>>

    @GET("/v3/top/anime/{page}/airing")
    fun getAiringAnimes(@Path("page") page: String): Single<Response<ResponseBody>>

    @GET("/v3/anime/{mal_id}")
    fun getDetailAnime(@Path("mal_id") malId: String):  Single<Response<ResponseBody>>


}