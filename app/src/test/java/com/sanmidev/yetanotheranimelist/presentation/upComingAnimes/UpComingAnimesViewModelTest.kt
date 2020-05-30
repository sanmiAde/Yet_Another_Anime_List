package com.sanmidev.yetanotheranimelist.presentation.upComingAnimes

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.javafaker.Faker
import com.nhaarman.mockito_kotlin.verify
import com.sanmidev.yetanotheranimelist.DataUtils
import com.sanmidev.yetanotheranimelist.NetworkTestUtils
import com.sanmidev.yetanotheranimelist.data.local.model.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.network.mapper.AnimeListMapper
import com.sanmidev.yetanotheranimelist.data.network.model.*
import com.sanmidev.yetanotheranimelist.data.network.repo.JikanRepository
import com.sanmidev.yetanotheranimelist.data.network.repo.JikanRepositoryImpl
import com.sanmidev.yetanotheranimelist.data.network.service.JikanService
import com.sanmidev.yetanotheranimelist.utils.TestAppScheduler
import com.squareup.moshi.Moshi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
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
class UpComingAnimesViewModelTest {

    @get:Rule
    val mockWebServer = MockWebServer()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var retrofit: Retrofit
    private lateinit var generatedData: Triple<AnimeListResponse, List<AnimeResponse>, List<AnimeEntity>>
    private lateinit var moshi: Moshi
    private lateinit var jikanService: JikanService
    private lateinit var jikanRepository: JikanRepository
    private lateinit var SUT: UpComingAnimesViewModel
    private val faker = Faker()
    private val animeListMapper = AnimeListMapper()


    @Mock
    lateinit var observer: Observer<AnimeListResult>

    @Mock
    lateinit var application: Application

    @Before
    fun setUp() {
        retrofit = NetworkTestUtils.provideRetrofit(mockWebServer)
        moshi = NetworkTestUtils.moshi
        generatedData = DataUtils.generateAnimeListResponse(faker)
        jikanService = retrofit.create(JikanService::class.java)
        jikanRepository = JikanRepositoryImpl(jikanService, animeListMapper, moshi)

    }

    @Test
    fun getUpComingAnimes_shouldReturnAnimeResultSuccess_whenInitialised() {
            //GIVEN
        mockWebServer.enqueue(
            MockResponse()
                .setBody(AnimeListResponseJsonAdapter(moshi).toJson(generatedData.first))
                .setResponseCode(HttpURLConnection.HTTP_OK)
        )

        //WHEN
        SUT = UpComingAnimesViewModel(jikanRepository, TestAppScheduler(), application)
        SUT.upComingLiveData.observeForever(observer)

        //THEN
        verify(observer).onChanged(any(AnimeListResult.success::class.java))
    }


    @Test
    fun getUpComingAnimes_shouldReturnAnimeResultAPIError_whenInitialised() {

        //GIVEN
        mockWebServer.enqueue(
            MockResponse()
                .setBody(AnimeListErrorResponesJsonAdapter(moshi).toJson(DataUtils.getAnimeListErrorResponse()))
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        )

        //WHEN
        SUT = UpComingAnimesViewModel(jikanRepository, TestAppScheduler(), application)
        SUT.upComingLiveData.observeForever(observer)

        //THEN
        verify(observer).onChanged(any(AnimeListResult.APIerror::class.java))
    }




    @After
    fun tearDown() {
    }
}