package com.sanmidev.yetanotheranimelist

import com.sanmidev.yetanotheranimelist.data.network.model.AnimeListResponseJsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkTestUtils {
     val moshi by lazy {

        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    }

    fun provideRetrofit(mockWebServer: MockWebServer) : Retrofit{
        return Retrofit.Builder()
// 1
                .baseUrl(mockWebServer.url("/"))
// 2
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
// 3
                .addConverterFactory(MoshiConverterFactory.create(moshi))
// 4
                .build()
        }




    }






