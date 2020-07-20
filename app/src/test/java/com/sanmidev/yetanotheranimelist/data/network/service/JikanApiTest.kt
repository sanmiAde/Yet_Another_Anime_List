package com.sanmidev.yetanotheranimelist.data.network.service

import com.github.javafaker.Faker
import com.google.common.truth.Truth
import com.sanmidev.yetanotheranimelist.DataUtils
import com.sanmidev.yetanotheranimelist.NetworkTestUtils
import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeListResponse
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeResponse
import com.squareup.moshi.Moshi
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit

class JikanApiTest {

    @get:Rule
    val mockWebServer = MockWebServer()

    private lateinit var retrofit: Retrofit
    private lateinit var generatedData: Triple<AnimeListResponse, List<AnimeResponse>, List<AnimeEntity>>
    private lateinit var moshi: Moshi
    private lateinit var SUT: JikanService
    private val faker = Faker()

    @Before
    fun setUp() {
        retrofit = NetworkTestUtils.provideRetrofit(mockWebServer)
        moshi = NetworkTestUtils.moshi
        generatedData = DataUtils.generateAnimeListResponse(faker)
        SUT = retrofit.create(JikanService::class.java)
    }


    @Test
    fun getUpcomingAnimeList_Calls_The_Correct_API_Successfully() {
        //GIVEN
        NetworkTestUtils.getAnimeListSuccessMockWebserver(mockWebServer, generatedData.first)

        //WHEN
        val testObserver = SUT.getUpandComingAnime("1").test()
        val request = mockWebServer.takeRequest()

        //THEN
        Truth.assertThat("/v3/top/anime/1/upcoming").isEqualTo(request.path)
        Truth.assertThat("GET").isEqualTo(request.method)
        testObserver.assertNoErrors()

    }


    @Test
    fun getAiringAnimeList_Calls_The_Correct_API_Successfully(){
        //GIVEN
        NetworkTestUtils.getAnimeListSuccessMockWebserver(mockWebServer, generatedData.first)

        //WHEN
        val testObserver = SUT.getAiringAnimes("1").test()
        val request = mockWebServer.takeRequest()

        //THEN
        Truth.assertThat("/v3/top/anime/1/airing").isEqualTo(request.path)
        Truth.assertThat("GET").isEqualTo(request.method)
        testObserver.assertNoErrors()
    }


    @Test
    fun getAnimeDetail_Calls_The_Correct_API_Successfully(){
        //GIVEN
        NetworkTestUtils.getAnimeListSuccessMockWebserver(mockWebServer, generatedData.first)

        //WHEN
        val testObserver = SUT.getDetailAnime("39587").test()
        val request = mockWebServer.takeRequest()


        //THEN
        Truth.assertThat("/v3/anime/39587").isEqualTo(request.path)
        Truth.assertThat("GET").isEqualTo(request.method)

    }

}