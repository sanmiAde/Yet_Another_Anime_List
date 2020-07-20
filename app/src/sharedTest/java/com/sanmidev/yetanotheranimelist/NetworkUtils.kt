package com.sanmidev.yetanotheranimelist

import com.sanmidev.yetanotheranimelist.data.local.model.animedetail.AnimeDetailEnitity
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.AnimeDetailResponse
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.AnimeDetailResponseJsonAdapter
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeListResponse
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeListResponseJsonAdapter
import com.sanmidev.yetanotheranimelist.data.network.model.error.JikanErrorRespone
import com.sanmidev.yetanotheranimelist.data.network.model.error.JikanErrorResponeJsonAdapter
import com.sanmidev.yetanotheranimelist.data.network.service.JikanService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.HttpURLConnection

object NetworkTestUtils {
    val moshi by lazy {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    fun provideRetrofit(mockWebServer: MockWebServer): Retrofit {
        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    fun provideJikanservice(mockWebServer: MockWebServer): JikanService {
        return provideRetrofit(mockWebServer).create(JikanService::class.java)
    }


    fun getAnimeListSuccessMockWebserver(
        mockWebServer: MockWebServer,
        generatedData: AnimeListResponse
    ) {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(AnimeListResponseJsonAdapter(moshi).toJson(generatedData))
                .setResponseCode(200)
        )
    }

    
    fun getAnimeListErrorResponseMockWebServer(
        mockWebServer: MockWebServer,
        errorRespone: JikanErrorRespone
    ) {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(JikanErrorResponeJsonAdapter(moshi).toJson(DataUtils.getAnimeListErrorResponse()))
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        )
    }


    fun getAnimeDetailDispatcher(data : Pair<AnimeDetailResponse, AnimeDetailEnitity>): Dispatcher {

       val dispatcher = object : Dispatcher() {

            override fun dispatch(request: RecordedRequest): MockResponse {
                return when {
                    request.path?.contains("/v3/anime/3983")!! -> {
                        MockResponse().setBody(
                            AnimeDetailResponseJsonAdapter(moshi).toJson(
                                data.first
                            )
                        )
                            .setResponseCode(HttpURLConnection.HTTP_OK)

                    }
                    request.path?.contains("/v3/anime/5000")!! -> {
                        MockResponse().setBody(
                            JikanErrorResponeJsonAdapter(moshi).toJson(
                                DataUtils.getAnimeListErrorResponse()
                             )
                        )
                            .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                    }

                    else -> MockResponse().setResponseCode(404)
                }
            }
        }

        return dispatcher
    }
}






