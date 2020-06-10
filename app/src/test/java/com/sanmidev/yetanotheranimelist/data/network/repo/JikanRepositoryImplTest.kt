package com.sanmidev.yetanotheranimelist.data.network.repo

import com.github.javafaker.Faker
import com.google.common.truth.Truth
import com.sanmidev.yetanotheranimelist.DataUtils
import com.sanmidev.yetanotheranimelist.NetworkTestUtils
import com.sanmidev.yetanotheranimelist.data.local.model.animedetail.AnimeDetailEnitity
import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.network.mapper.AnimeDetailMapper
import com.sanmidev.yetanotheranimelist.data.network.mapper.AnimeListMapper
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.AnimeDetailResponse
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.AnimeDetailResult
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeListResponse
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeListResponseJsonAdapter

import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeListResult
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeResponse
import com.sanmidev.yetanotheranimelist.data.network.service.JikanService
import com.squareup.moshi.Moshi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import java.net.HttpURLConnection

class JikanRepositoryImplTest {


    @get:Rule
    val mockWebServer = MockWebServer()

    private lateinit var retrofit: Retrofit
    private lateinit var generatedAnimeListData: Triple<AnimeListResponse, List<AnimeResponse>, List<AnimeEntity>>
    private lateinit var generatedAnimeDetailData : Pair<AnimeDetailResponse, AnimeDetailEnitity>
    private lateinit var moshi: Moshi
    private lateinit var jikanService: JikanService
    private lateinit var SUT: JikanRepository
    private val faker = Faker()
    private val animeListMapper = AnimeListMapper()
    private val animeDetailMapper = AnimeDetailMapper()
    private val fakeSaas = FakeSaas()

    @Before
    fun setUp() {
        retrofit = NetworkTestUtils.provideRetrofit(mockWebServer)
        moshi = NetworkTestUtils.moshi
        generatedAnimeListData = DataUtils.generateAnimeListResponse(faker)
        generatedAnimeDetailData = DataUtils.generateAnimeDetailData(faker)
        jikanService = retrofit.create(JikanService::class.java)
        SUT = JikanRepositoryImpl(jikanService, animeListMapper, animeDetailMapper, moshi, fakeSaas)
    }

    @Test
    fun getUpComingAnimes_shouldReturnAnimeEnitiesList_whenRequestIsSuccesfull() {

        //GIVEN
        mockWebServer.enqueue(
            MockResponse()
                .setBody(AnimeListResponseJsonAdapter(moshi).toJson(generatedAnimeListData.first))
                .setResponseCode(HttpURLConnection.HTTP_OK)
        )

        //WHEN
        val testObserver = SUT.getUpComingAnimeList(1).test()
        val result = testObserver.values()[0] as AnimeListResult.Success


        //THEN
        Truth.assertThat(result.data.animeEnities).containsExactlyElementsIn(generatedAnimeListData.third)
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
        Truth.assertThat(result.jikanErrorRespone)
            .isEqualTo(DataUtils.getAnimeListErrorResponse())
    }


//    fun AnimeListResponseJsonAdapter_ShouldReturnListOfAnime_WhenActualDataIsUsed() {
//        val animeListResponseJsonAdapter =
//            AnimeListResponseJsonAdapter(moshi).fromJson(DataUtils.animeListtestJSonData)
//
//        Truth.assertThat(animeListResponseJsonAdapter!!.animeResponses).hasSize(50)
//    }


    @Test
    fun getCurrentlyAiringAnimes_shouldReturnListOfCurrentlyAiringAnimes_whenRequestIsSuccessful() {
        //GIVEN
        NetworkTestUtils.initAnimeListSuccessMockWebserver(mockWebServer, generatedAnimeListData.first)

        //WHEN
        val testObserver = SUT.getAiringAnimes(1).test()
        val result = testObserver.values()[0] as AnimeListResult.Success


        //THEN
        Truth.assertThat(result.data.animeEnities).containsExactlyElementsIn(generatedAnimeListData.third)

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
        Truth.assertThat(result.jikanErrorRespone)
            .isEqualTo(DataUtils.getAnimeListErrorResponse())
    }

    @Test
    fun getAnimeDetail_shouldReturnSuccessResponse_whenRequestIsSuccessful() {

        //GIVEN
        mockWebServer.dispatcher = NetworkTestUtils.getAnimeDetailDispatcher(generatedAnimeDetailData)


        //WHEN
        val testObserver = SUT.getAnimeDetail(3983).test()
        val result = testObserver.values()[0] as AnimeDetailResult.Success


        //THEN
        Truth.assertThat(result.data).isEqualTo(generatedAnimeDetailData.second)

    }

    @Test
    fun getAnimeDetail_shouldReturnErrorResponse_whenResourceIsNotFound(){
        //GIVEN
        mockWebServer.dispatcher = NetworkTestUtils.getAnimeDetailDispatcher(generatedAnimeDetailData)

        //WHEN
        val testObserver = SUT.getAnimeDetail(5000).test()
        val result = testObserver.values()[0] as AnimeDetailResult.APIerror

        //THEN
        Truth.assertThat(result.jikanErrorRespone).isEqualTo(DataUtils.getAnimeListErrorResponse())

    }


    @After
    fun tearDown() {

    }
}