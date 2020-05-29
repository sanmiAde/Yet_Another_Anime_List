package com.sanmidev.yetanotheranimelist.data.network.repo

import com.sanmidev.yetanotheranimelist.data.network.mapper.AnimeListMapper
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeListErrorResponesJsonAdapter
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeListResponseJsonAdapter
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeListResult
import com.sanmidev.yetanotheranimelist.data.network.service.JikanService
import com.squareup.moshi.Moshi
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.HttpURLConnection
import javax.inject.Inject

/***
 * Concrete implementation fo [JikanRepository]
 * Used to interact with APIs from Jikan Anime Service.
 */
class JikanRepositoryImpl @Inject constructor(
    val jikanService: JikanService,
    val animeListMapper: AnimeListMapper,
    val moshi: Moshi
) : JikanRepository {
    /***
     * Gets upcoming animes from the jikan end point and maps to the respective entity depending on the status code returned.
     * @param page is the current page on the  MAL website.
     *
     *
     */
    override fun getUpComingAnimeList(page: Int): Single<AnimeListResult> {
        return jikanService.getUpandComingAnime(page.toString())
            .map { responseBody: Response<ResponseBody> ->
                when (responseBody.code()) {
                    HttpURLConnection.HTTP_OK -> {
                        val successResult =
                            AnimeListResponseJsonAdapter(moshi).fromJson(requireNotNull(responseBody.body()).string())

                        successResult?.let { animeListResponse ->
                            val animeEnities = animeListMapper.transformAnimeListToEntity(
                                animeListResponse
                            )
                            AnimeListResult.success(animeEnities)
                        }

                    }

                    else -> {
                        val errorResult = AnimeListErrorResponesJsonAdapter(moshi).fromJson(
                            requireNotNull(responseBody.errorBody()).string()
                        )
                        errorResult?.let {
                            AnimeListResult.APIerror(it)
                        }
                    }
                }
            }
    }
}