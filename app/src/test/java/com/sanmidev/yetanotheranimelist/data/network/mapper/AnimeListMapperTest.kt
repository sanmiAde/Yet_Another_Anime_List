package com.sanmidev.yetanotheranimelist.data.network.mapper

import com.github.javafaker.Faker
import com.google.common.truth.Truth
import com.sanmidev.yetanotheranimelist.DataUtils
import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeListResponse
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeResponse
import org.junit.After
import org.junit.Before
import org.junit.Test

class AnimeListMapperTest {

    private lateinit var animeListResponse: AnimeListResponse
    private lateinit var animeReponses: List<AnimeResponse>
    private lateinit var animeEnities: List<AnimeEntity>
    private val faker = Faker()
    private lateinit var SUT: AnimeListMapper

    @Before
    fun setUp() {
        val generatedData = DataUtils.generateAnimeListResponse(faker)
        animeListResponse = generatedData.first
        animeReponses = generatedData.second
        animeEnities = generatedData.third
    }

    @Test
    fun transformAnimeListToEntity_AnimeListResponse_ShouldReturnsCorrectSize() {
        //Given
        SUT = AnimeListMapper()


        //When
        val animeEntityList = SUT.transformAnimeListToEntity(animeListResponse)

        //THen
        Truth.assertThat(animeEntityList.animeEnities).hasSize(50)

    }


    @Test
    fun transformAnimeListToEntity_AnimeListResponse_ShouldReturnsCorrectData() {
        //Given
        SUT = AnimeListMapper()
        //When
        val animeEntityList = SUT.transformAnimeListToEntity(animeListResponse)
        //THen

        Truth.assertThat(animeEntityList.animeEnities).containsExactlyElementsIn(animeEnities)

    }




    @After
    fun tearDown() {
    }
}