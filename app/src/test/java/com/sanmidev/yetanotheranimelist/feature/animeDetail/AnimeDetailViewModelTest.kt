package com.sanmidev.yetanotheranimelist.feature.animeDetail

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.github.javafaker.Faker
import org.mockito.ArgumentMatchers.any
import com.nhaarman.mockito_kotlin.verify
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
import com.sanmidev.yetanotheranimelist.data.network.repo.FakeSaas
import com.sanmidev.yetanotheranimelist.data.network.repo.JikanRepository
import com.sanmidev.yetanotheranimelist.data.network.repo.JikanRepositoryImpl
import com.sanmidev.yetanotheranimelist.data.network.service.JikanService
import com.sanmidev.yetanotheranimelist.feature.upComingAnimes.UpComingAnimesViewModel
import com.sanmidev.yetanotheranimelist.utils.TestAppScheduler
import com.squareup.moshi.Moshi
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import java.net.HttpURLConnection

@RunWith(MockitoJUnitRunner::class)
class AnimeDetailViewModelTest {

    @get:Rule
    val mockWebServer = MockWebServer()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var retrofit: Retrofit
    private lateinit var generatedData: Pair<AnimeDetailResponse, AnimeDetailEnitity>
    private lateinit var moshi: Moshi
    private lateinit var jikanService: JikanService
    private lateinit var jikanRepository: JikanRepository
    private lateinit var SUT: AnimeDetailViewModel
    private val faker = Faker()
    private val animeListMapper = AnimeListMapper()
    private val animeDetailMapper = AnimeDetailMapper()
    private  lateinit var dispatcher : Dispatcher
    private val fakeSaas = FakeSaas()

    @Mock
    lateinit var observer: Observer<AnimeDetailResult>

    @Mock
    lateinit var application: Application


    @Before
    fun setUp() {
        retrofit = NetworkTestUtils.provideRetrofit(mockWebServer)
        moshi = NetworkTestUtils.moshi
        generatedData = DataUtils.generateAnimeDetailData(faker)
        jikanService = retrofit.create(JikanService::class.java)
        jikanRepository = JikanRepositoryImpl(jikanService, animeListMapper,animeDetailMapper, moshi, fakeSaas)


        dispatcher = NetworkTestUtils.getAnimeDetailDispatcher(generatedData)
    }


    @Test
    fun getAnimeDetail_shouldReturnAnimeResultSuccess_whenViewModelIsInitialisedAndRequestisSuccessful(){
        //GIVEN
        mockWebServer.dispatcher =  dispatcher

        //WHEN
        SUT = AnimeDetailViewModel(jikanRepository, TestAppScheduler(), application,
            SavedStateHandle( mapOf(Pair(AnimeDetailFragment.DETAIL_ANIME_ID_KEY, 3983))))

        SUT.animeDetailResultState.observeForever(observer)

        //THEN
        verify(observer).onChanged(any(AnimeDetailResult.Success::class.java))
    }


    @Test
    fun getAnimeDetail_shouldReturnAnimeResultApiError_whenViewModelIsInitialisedAndResourceIsNotFound(){
        mockWebServer.dispatcher =  dispatcher

        //WHEN
        SUT = AnimeDetailViewModel(jikanRepository, TestAppScheduler(), application,
            SavedStateHandle( mapOf(Pair(AnimeDetailFragment.DETAIL_ANIME_ID_KEY, 5000))))

        SUT.animeDetailResultState.observeForever(observer)

        //THEN
        verify(observer).onChanged(any(AnimeDetailResult.APIerror::class.java))
    }

    @After
    fun tearDown() {
    }
}