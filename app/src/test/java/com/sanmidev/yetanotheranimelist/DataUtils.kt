package com.sanmidev.yetanotheranimelist

import com.github.javafaker.Faker
import com.sanmidev.yetanotheranimelist.data.local.model.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeListResponse
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeResponse

object DataUtils {

    fun generateAnimeReponse(faker: Faker): Pair<MutableList<AnimeResponse>, MutableList<AnimeEntity>> {
        val animeReponseList = mutableListOf<AnimeResponse>()
        val animeEnityList = mutableListOf<AnimeEntity>()


        for (i in 1..50) {
            val endDate = faker.date().birthday().toString()
            val episodes = faker.number().randomDigit()
            val imageUrl = faker.internet().url()
            val malId = faker.number().randomDigit()
            val members = faker.number().randomDigit()
            val rank = faker.number().randomDigit()
            val score = faker.number().randomDigit().toDouble()
            val startDate = faker.date().birthday().toString()
            val title = faker.name().title()
            val type = faker.music().genre()
            val url = faker.internet().url()

            val animeResponse =
                AnimeResponse(
                    endDate,
                    episodes,
                    imageUrl,
                    malId,
                    members,
                    rank,
                    score,
                    startDate,
                    title,
                    type,
                    url
                )


            val animeEntity = AnimeEntity(
                endDate, episodes, imageUrl, malId, score, startDate, title, type, url
            )

            animeReponseList.add(animeResponse)

            animeEnityList.add(animeEntity)
        }

        return Pair(animeReponseList, animeEnityList)
    }


    fun generateAnimeListResponse(faker: Faker): Triple<AnimeListResponse, List<AnimeResponse>, List<AnimeEntity>> {

        val animeLists = generateAnimeReponse(faker)


        return Triple(
            AnimeListResponse(
                faker.hashCode(), true, faker.hashCode().toString(),
                animeLists.first

            ), animeLists.first, animeLists.second
        )
    }
}