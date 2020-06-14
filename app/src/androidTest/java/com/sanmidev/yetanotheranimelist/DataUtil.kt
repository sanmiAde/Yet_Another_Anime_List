package com.sanmidev.yetanotheranimelist



import com.github.javafaker.Faker
import com.sanmidev.yetanotheranimelist.data.local.model.animedetail.*
import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.*
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeListResponse
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeResponse
import com.sanmidev.yetanotheranimelist.data.network.model.error.JikanErrorRespone

object AndroidDataUtils {

    var genreResponse: MutableList<GenreResponse> = mutableListOf()

    var genreEntity: MutableList<GenreEntity> = mutableListOf()

    var animeDetailEnitity: AnimeDetailEnitity? = null

    val animeDetailResponse: AnimeDetailResponse? = null

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


            val animeEntity =
                AnimeEntity(
                    imageUrl, malId, title
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

    fun getAnimeListErrorResponse(): JikanErrorRespone {
        return JikanErrorRespone(
            "Resource does not exist",
            "Something Happened",
            404,
            "BadResponseException"
        )
    }

    fun generateGenreResponse(faker: Faker): Pair<List<GenreResponse>, List<GenreEntity>> {


        repeat(10) {
            val malId = faker.number().digit().toInt()
            val name = faker.name().firstName()
            val type = faker.music().genre()
            val url = faker.internet().url()

            genreResponse.add(GenreResponse(malId, name, type, url))

            genreEntity.add(GenreEntity(malId, name, type, url))


        }

        return Pair(genreResponse, genreEntity)

    }


    fun generateToData(faker: Faker): Pair<ToResponse, ToEntity> {
        val month = faker.name().name()
        val day = faker.name().title()
        val year = faker.name().lastName()

        val toResponse =
            ToResponse(day, month, year)
        val toEntity = ToEntity(day, month, year)

        return Pair(toResponse, toEntity)
    }

    fun generateFromData(faker: Faker): Pair<FromResponse, FromEntity> {
        val month = faker.name().name()
        val day = faker.name().title()
        val year = faker.name().lastName()

        val fromResponse =
            FromResponse(day, month, year)
        val fromEntity = FromEntity(day, month, year)


        return Pair(fromResponse, fromEntity)
    }

    fun generatePropData(faker: Faker): Pair<PropResponse, PropEntity> {
        val from = generateFromData(faker)
        val to = generateToData(faker)
        val propResponse = PropResponse(from.first, to.first)
        val propEntity = PropEntity(from.second, to.second)

        return Pair(propResponse, propEntity)
    }

    fun generateAiredData(faker: Faker): Pair<AiredResponse, AiredEntity> {

        val propData = generatePropData(faker)

        val from: String = faker.address().city()
        val to: String = faker.address().cityName()
        val string: String = faker.address().country()

        val airedResponse = AiredResponse(from, propData.first, string, to)
        val airedEntity = AiredEntity(from, propData.second, string, to)

        return Pair(airedResponse, airedEntity)

    }

    fun generateAnimeDetailData(faker: Faker): Pair<AnimeDetailResponse, AnimeDetailEnitity> {

        val airedData: Pair<AiredResponse, AiredEntity> = generateAiredData(faker)

        val genreData: Pair<List<GenreResponse>, List<GenreEntity>> = generateGenreResponse(faker)

        val endingTheme = mutableListOf(faker.music().genre(), faker.music().genre())

        val licensorsList = mutableListOf(faker.music().genre(), faker.music().genre())

        val openingThemSong = mutableListOf(faker.music().genre(), faker.music().genre())

        val producersList = mutableListOf(faker.music().genre(), faker.music().genre())

        val titleSynonymsList = mutableListOf(faker.music().genre(), faker.music().genre())

        val adaptationResponseList = generateAdaptionResponse(faker, 5)

        val sequelResponseList = generateSequelResponse(faker, 2)

        val prequelResponseList = generatePrequelResponse(faker, 50)

        val studioResponseList = generateStudioResponse(faker, 23)


        val relatedResponseList = RelatedResponse(
            adaptation = adaptationResponseList,
            prequelResponse = prequelResponseList,
            sequelResponse = sequelResponseList
        )


        val aired = airedData.first
        val airing = faker.bool().bool()
        val background = faker.address().cityName()
        val broadcast = faker.ancient().god()
        val duration = faker.number().digit()
        val endingThemes = endingTheme
        val episodes = faker.number().randomDigit()
        val favorites = faker.number().randomDigit()
        val genreResponses = genreData.first
        val imageUrl = faker.internet().url()
        val licensors = licensorsList
        val malId = faker.number().randomDigit()
        val openingThemes = openingThemSong
        val popularity = faker.number().randomDigit()
        val premiered = faker.date().toString()
        val producers = producersList
        val rank = faker.backToTheFuture().character()
        val rating = faker.company().buzzword()
        val relatedResponse = relatedResponseList
        val requestCacheExpiry = faker.number().randomDigit()
        val requestCached = faker.bool().bool()
        val requestHash = faker.idNumber().valid()
        val score = faker.number().digits(12)
        val scoredBy = faker.number().digits(12)
        val status = faker.name().title()
        val studioResponses = studioResponseList
        val synopsis = faker.name().title()
        val title = faker.name().name()
        val titleEnglish = faker.name().name()
        val titleJapanese = faker.name().name()
        val trailerUrl = faker.internet().url()
        val type = faker.name().lastName()
        val url = faker.name().bloodGroup()
        val members = faker.number().randomDigit()
        val source = faker.internet().url()
        val titleSynonyms = titleSynonymsList


        val animeDetailResponse = AnimeDetailResponse(
            aired,
            airing,
            background,
            broadcast,
            duration,
            endingThemes,
            episodes,
            favorites,
            genreResponses,
            imageUrl,
            licensors,
            malId,
            openingThemes = openingThemes,
            popularity = popularity,
            premiered = premiered,
            producers = producers,
            rank = rank,
            rating = rating,
            relatedResponse = relatedResponse,
            requestCacheExpiry = requestCacheExpiry,
            requestCached = requestCached,
            requestHash = requestHash,
            score = score,
            scoredBy = scoredBy,
            status = status,
            studioResponses = studioResponseList,
            synopsis = synopsis,
            title = title,
            titleEnglish = titleEnglish,
            titleJapanese = titleJapanese,
            trailerUrl = trailerUrl,
            type = type,
            url = url,
            members = members,
            source = source,
            titleSynonyms = titleSynonyms

        )


        val animeDetailEnitity = AnimeDetailEnitity(
            aired = airedData.second,
            airing = airing,
            broadcast = broadcast,
            duration = duration,
            endingThemes = endingThemes,
            episodes = episodes,
            genreEntity = genreData.second,
            imageUrl = imageUrl,
            id = malId,
            openingThemes = openingThemSong,
            premiered = premiered,
            rating = rating,
            trailerUrl = trailerUrl,
            score = score,
            status = status,
            synopsis = synopsis,
            title = title,
            type = type,
            url = url

        )

        return Pair(animeDetailResponse, animeDetailEnitity)
    }


    fun generateAdaptionResponse(faker: Faker, size: Int): List<AdaptationResponse> {
        val adaption = mutableListOf<AdaptationResponse>()

        repeat(size) {
            val adaptationResponse = AdaptationResponse(
                faker.number().randomDigit(),
                faker.name().name(),
                faker.name().title(),
                faker.internet().url()
            )
            adaption.add(adaptationResponse)
        }

        return adaption

    }

    fun generatePrequelResponse(faker: Faker, size: Int): List<PrequelResponse> {
        val adaption = mutableListOf<PrequelResponse>()

        repeat(size) {
            val prequelResponse = PrequelResponse(
                faker.number().randomDigit(),
                faker.name().name(),
                faker.name().title(),
                faker.internet().url()
            )
            adaption.add(prequelResponse)
        }

        return adaption
    }

    fun generateSequelResponse(faker: Faker, size: Int): List<SequelResponse> {
        val adaption = mutableListOf<SequelResponse>()

        repeat(size) {
            val sequelResponse = SequelResponse(
                faker.number().randomDigit(),
                faker.name().name(),
                faker.name().title(),
                faker.internet().url()
            )
            adaption.add(sequelResponse)
        }

        return adaption

    }


    fun generateStudioResponse(faker: Faker, size: Int): List<StudioResponse> {
        val adaption = mutableListOf<StudioResponse>()

        repeat(size) {
            val sequelResponse = StudioResponse(
                faker.number().randomDigit(),
                faker.name().name(),
                faker.name().title(),
                faker.internet().url()
            )
            adaption.add(sequelResponse)
        }

        return adaption

    }


}