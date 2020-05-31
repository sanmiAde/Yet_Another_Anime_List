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
    private val jikanService: JikanService,
    private val animeListMapper: AnimeListMapper,
    private val moshi: Moshi,
    private val saas: Saas
) : JikanRepository {

    /***
     * Gets upcoming animes from the jikan end point and maps to the respective entity depending on the status code returned.
     * @param page is the current page on the  MAL website.
     **
     */
    override fun getUpComingAnimeList(page: Int): Single<AnimeListResult> {
        return jikanService.getUpandComingAnime(page.toString())
            .map { responseBody: Response<ResponseBody> ->
                processAnimeListResponse(responseBody)
            }
    }

    /***
    * Gets gets currently airing animes from the jikan end point and maps to the respective entity depending on the status code returned.
    * @param page is the current page on the  MAL website.
    **
    */
    override fun getAiringAnimes(page: Int): Single<AnimeListResult> {
        return jikanService.getAiringAnimes(page.toString())
            .map { responseBody: Response<ResponseBody> ->
                processAnimeListResponse(responseBody)
            }
    }

    /***
     * Converts the responsebody to an [AnimeListResult]
     * @param responseBody is the response from the jikan server
     * @return [AnimeListResult] mapped from the response body.
     */
    private fun processAnimeListResponse(responseBody: Response<ResponseBody>): AnimeListResult? {

        return when (responseBody.code()) {
            HttpURLConnection.HTTP_OK -> {
                try {
                    val successResult =
                        AnimeListResponseJsonAdapter(moshi).fromJson(requireNotNull(responseBody.body()).string())

                    successResult?.let { animeListResponse ->
                        val animeEnities = animeListMapper.transformAnimeListToEntity(
                            animeListResponse
                        )
                        AnimeListResult.success(animeEnities)
                    }
                } catch (ex: Exception) {
                    saas.logException(ex)
                    AnimeListResult.Exception("Error occured.", Throwable(ex))
                }


            }

            else -> {
                try {
                    val errorResult = AnimeListErrorResponesJsonAdapter(moshi).fromJson(
                        requireNotNull(responseBody.errorBody()).string()
                    )
                    errorResult?.let {
                        AnimeListResult.APIerror(it)
                    }
                } catch (ex: Exception) {
                    saas.logException(ex)
                    AnimeListResult.Exception("Error occured.", Throwable(ex))
                }

            }
        }
    }
}