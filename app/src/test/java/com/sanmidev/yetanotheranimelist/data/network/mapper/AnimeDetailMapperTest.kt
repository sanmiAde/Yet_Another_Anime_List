package com.sanmidev.yetanotheranimelist.data.network.mapper

import com.github.javafaker.Faker
import com.google.common.truth.Truth
import com.sanmidev.yetanotheranimelist.DataUtils
import org.junit.Before
import org.junit.Test

class AnimeDetailMapperTest {

    lateinit var faker: Faker
    lateinit var mapper: AnimeDetailMapper

    @Before
    fun setUp() {
        faker = Faker()

        mapper = AnimeDetailMapper()
    }

    @Test
    fun tranformAnimeDetailToAnimeEntity() {
    }

    @Test
    fun transformGenreReponseToGenreEntity() {
    }

    @Test
    fun tranformGenreResponseToFromEntity_shouldReturnGenreEntity_whenRequestIsSuccessful(){
        //GIVEN
        val data = DataUtils.generateGenreResponse(faker)
        //WHEN
       val transformedGenreEntity =  mapper.transformGenreReponseToGenreEntity(data.first)

        //THEN
        Truth.assertThat(transformedGenreEntity).containsExactlyElementsIn(data.second)

    }


    @Test
    fun tranformToReponseToToEntity_shouldReturnToResponseEntity_whenRequestIsSuccesful(){
        //GIVEN
        val data = DataUtils.generateToData(faker)
        //WHEN
        val tranformedToEntity = mapper.tranformToResponseToToEntity(data.first)

        //THEN
        Truth.assertThat(tranformedToEntity).isEqualTo(data.second)
    }


    @Test
    fun tranformFromReponseToFromEntity_shouldReturnToResponseEntity_whenRequestIsSuccesful(){
        val data = DataUtils.generateFromData(faker)
        //WHEN
        val tranformedToEntity = mapper.tranformFromResponseToFromEntity(data.first)

        //THEN
        Truth.assertThat(tranformedToEntity).isEqualTo(data.second)
    }

    @Test
    fun transformPropResponseToPropEnitity_shouldReturnPropEntity_whenRequestIsSuccessful(){
        //GIVEN
        val data = DataUtils.generatePropData(faker)

        //WHEN
        val transformedEntity = mapper.tranformPropResponseToPropEntity(data.first)

        //THEN
        Truth.assertThat(transformedEntity).isEqualTo(data.second)
    }

    @Test
    fun transformAiredResponseToAiredEntity_shouldReturnAiredEntity_whenRequestIsSuccessful(){
        //GIVEN
        val data = DataUtils.generateAiredData(faker)

        //WHEN
        val transformEntity = mapper.transformAiredResponseToAiredEntity(data.first)

        //THEN
        Truth.assertThat(transformEntity).isEqualTo(data.second)
    }

    @Test
    fun tranformAnimeDetailToAnimeEntity_shouldReturnAnimeEntity_whenRequestIsSuccessful(){
        //GIVEN
        val data = DataUtils.generateAnimeDetailData(faker)

        val transfromEntity = mapper.tranformAnimeDetailToAnimeEntity(data.first)

        //THEN
        Truth.assertThat(transfromEntity).isEqualTo(data.second)
    }
}