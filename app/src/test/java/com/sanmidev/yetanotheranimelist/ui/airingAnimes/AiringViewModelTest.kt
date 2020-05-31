package com.sanmidev.yetanotheranimelist.ui.airingAnimes

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.javafaker.Faker
import com.google.common.truth.Truth
import com.nhaarman.mockito_kotlin.verify
import com.sanmidev.yetanotheranimelist.DataUtils
import com.sanmidev.yetanotheranimelist.NetworkTestUtils
import com.sanmidev.yetanotheranimelist.data.local.model.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.network.mapper.AnimeListMapper
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeListResponse
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeListResponseJsonAdapter
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeListResult
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeResponse
import com.sanmidev.yetanotheranimelist.data.network.repo.FakeSaas
import com.sanmidev.yetanotheranimelist.data.network.repo.JikanRepository
import com.sanmidev.yetanotheranimelist.data.network.repo.JikanRepositoryImpl
import com.sanmidev.yetanotheranimelist.data.network.repo.Saas
import com.sanmidev.yetanotheranimelist.data.network.service.JikanService
import com.sanmidev.yetanotheranimelist.utils.TestAppScheduler
import com.squareup.moshi.Moshi
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import java.net.HttpURLConnection

@RunWith(MockitoJUnitRunner::class)
class AiringViewModelTest {

    @get:Rule
    val mockWebServer = MockWebServer()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var retrofit: Retrofit
    private lateinit var generatedData: Triple<AnimeListResponse, List<AnimeResponse>, List<AnimeEntity>>
    private lateinit var moshi: Moshi
    private lateinit var jikanService: JikanService
    private lateinit var jikanRepository: JikanRepository
    private lateinit var SUT: AiringViewModel
    private lateinit var dispatcher: Dispatcher
    private val faker = Faker()
    private val animeListMapper = AnimeListMapper()
    private val testAppScheduler = TestAppScheduler()
    private val fakeSaas = FakeSaas()

    @Mock
    lateinit var observer: Observer<AnimeListResult>

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var applicationContext: Context


    @Before
    fun setup() {
        retrofit = NetworkTestUtils.provideRetrofit(mockWebServer)
        moshi = NetworkTestUtils.moshi
        generatedData = DataUtils.generateAnimeListResponse(faker)
        jikanService = retrofit.create(JikanService::class.java)
        jikanRepository = JikanRepositoryImpl(jikanService, animeListMapper, moshi, fakeSaas)


        dispatcher = object : Dispatcher() {

            override fun dispatch(request: RecordedRequest): MockResponse {
                return when {
                    request.path?.contains("/v3/top/anime/1/airing")!! -> {
                        MockResponse().setBody(
                            AnimeListResponseJsonAdapter(NetworkTestUtils.moshi).toJson(
                                generatedData.first
                            )
                        )
                            .setResponseCode(HttpURLConnection.HTTP_OK)

                    }
                    request.path?.contains("/v3/top/anime/2/airing")!! -> {
                        MockResponse().setBody(
                            AnimeListResponseJsonAdapter(NetworkTestUtils.moshi).toJson(
                                generatedData.first
                            )
                        )
                            .setResponseCode(HttpURLConnection.HTTP_OK)
                    }

                    else -> MockResponse().setResponseCode(404)
                }
            }
        }
    }

    @Test
    fun getAiringAnimes_shouldReturnAnimeResultSuccess_whenInitialised() {
        //GIVEN
        mockWebServer.dispatcher = dispatcher

        //WHEN
        SUT = AiringViewModel(jikanRepository, testAppScheduler)
        SUT.airingLiveData.observeForever(observer)

        //THEN
        verify(observer).onChanged(any(AnimeListResult.success::class.java))
    }


    @Test
    fun getNextAiringAnimes_shouldReturnAnimeResultSuccess_whenRequestIsSuccesful() {
        //GIVEN
        mockWebServer.dispatcher = dispatcher

        //WHEN
        SUT = AiringViewModel(jikanRepository, testAppScheduler)
        SUT.getNextAiringAnime()
        SUT.nextAiringLiveData.observeForever(observer)


        //THEN
        verify(observer).onChanged(any(AnimeListResult.success::class.java))
    }


    @Test
    fun getNextAiringAnimes_currentPageShouldEquals2_whenRequestIsSuccessful(){
        //GIVEN
        mockWebServer.dispatcher = dispatcher

        //WHEN
        SUT = AiringViewModel(jikanRepository, testAppScheduler)
        SUT.getNextAiringAnime()
        SUT.nextAiringLiveData.observeForever(observer)

        //THEN
        Truth.assertThat(SUT.currentPage).isEqualTo(2)

    }


    @Test
    fun getNextAiringAnimes_currentPageShouldEquals1_whenErrorOccurs(){

        //WHEN
        SUT = AiringViewModel(jikanRepository, testAppScheduler)
        SUT.getNextAiringAnime()
        SUT.nextAiringLiveData.observeForever(observer)

        //THEN
        Truth.assertThat(SUT.currentPage).isEqualTo(1)
    }
}