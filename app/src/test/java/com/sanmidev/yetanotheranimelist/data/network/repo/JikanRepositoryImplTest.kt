package com.sanmidev.yetanotheranimelist.data.network.repo

import com.github.javafaker.Faker
import com.google.common.truth.Truth
import com.sanmidev.yetanotheranimelist.DataUtils
import com.sanmidev.yetanotheranimelist.NetworkTestUtils
import com.sanmidev.yetanotheranimelist.data.local.model.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.network.mapper.AnimeListMapper
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeListResponse
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeListResponseJsonAdapter
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeListResult
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeResponse
import com.sanmidev.yetanotheranimelist.data.network.service.JikanService
import com.squareup.moshi.Moshi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import retrofit2.Retrofit
import java.net.HttpURLConnection

class JikanRepositoryImplTest {


    @get:Rule
    val mockWebServer = MockWebServer()

    private lateinit var retrofit: Retrofit
    private lateinit var generatedData: Triple<AnimeListResponse, List<AnimeResponse>, List<AnimeEntity>>
    private lateinit var moshi: Moshi
    private lateinit var jikanService: JikanService
    private lateinit var SUT: JikanRepository
    private val faker = Faker()
    private val animeListMapper = AnimeListMapper()
    private val fakeSaas = FakeSaas()

    @Before
    fun setUp() {
        retrofit = NetworkTestUtils.provideRetrofit(mockWebServer)
        moshi = NetworkTestUtils.moshi
        generatedData = DataUtils.generateAnimeListResponse(faker)
        jikanService = retrofit.create(JikanService::class.java)
        SUT = JikanRepositoryImpl(jikanService, animeListMapper, moshi, fakeSaas)
    }

    @Test
    fun getUpComingAnimes_shouldReturnAnimeEnitiesList_whenRequestIsSuccesfull() {

        //GIVEN
        mockWebServer.enqueue(
            MockResponse()
                .setBody(AnimeListResponseJsonAdapter(moshi).toJson(generatedData.first))
                .setResponseCode(HttpURLConnection.HTTP_OK)
        )

        //WHEN
        val testObserver = SUT.getUpComingAnimeList(1).test()
        val result = testObserver.values()[0] as AnimeListResult.success


        //THEN
        Truth.assertThat(result.data.animeEnities).containsExactlyElementsIn(generatedData.third)
    }


    @Test

    fun getUpComingAnimes_shouldReturnError404_whenResourceDoesNotExist() {
        //GIVEN
        NetworkTestUtils.initAnimeListErrorResponseMockWebServer(
            mockWebServer,
            DataUtils.getAnimeListErrorResponse()
        )

        //WHEN
        val testObserver = SUT.getUpComingAnimeList(400).test()
        val result = testObserver.values()[0] as AnimeListResult.APIerror


        //THEN
        Truth.assertThat(result.animeListErrorRespones)
            .isEqualTo(DataUtils.getAnimeListErrorResponse())
    }

    @Test
    fun AnimeListResponseJsonAdapter_ShouldReturnListOfAnime_WhenActualDataIsUsed() {
        val animeListResponseJsonAdapter =
            AnimeListResponseJsonAdapter(moshi).fromJson(DataUtils.animeListtestJSonData)

        Truth.assertThat(animeListResponseJsonAdapter!!.animeResponses).hasSize(50)
    }


    @Test
    fun getCurrentlyAiringAnimes_shouldReturnListOfCurrentlyAiringAnimes_whenRequestIsSuccessful() {
        //GIVEN
        NetworkTestUtils.initAnimeListSuccessMockWebserver(mockWebServer, generatedData.first)

        //WHEN
        val testObserver = SUT.getAiringAnimes(1).test()
        val result = testObserver.values()[0] as AnimeListResult.success


        //THEN
        Truth.assertThat(result.data.animeEnities).containsExactlyElementsIn(generatedData.third)

    }


    @Test
    fun getCurrentlyAiringAnimes_shouldReturnErrorResponse_whenResourceDoesNotExist() {
        //GIVEN
        NetworkTestUtils.initAnimeListErrorResponseMockWebServer(
            mockWebServer,
            DataUtils.getAnimeListErrorResponse()
        )

        //WHEN
        val testObserver = SUT.getAiringAnimes(500).test()
        val result = testObserver.values()[0] as AnimeListResult.APIerror

        //THEN
        Truth.assertThat(result.animeListErrorRespones)
            .isEqualTo(DataUtils.getAnimeListErrorResponse())
    }

    @After
    fun tearDown() {

    }
}