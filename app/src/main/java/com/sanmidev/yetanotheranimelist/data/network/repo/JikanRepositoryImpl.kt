package com.sanmidev.yetanotheranimelist.data.network.repo

import com.sanmidev.yetanotheranimelist.data.network.mapper.AnimeDetailMapper
import com.sanmidev.yetanotheranimelist.data.network.mapper.AnimeListMapper
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.AnimeDetailResponse
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.AnimeDetailResponseJsonAdapter
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.AnimeDetailResult
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeListResponseJsonAdapter

import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeListResult
import com.sanmidev.yetanotheranimelist.data.network.model.error.JikanErrorResponeJsonAdapter
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
    private val animeDetailMapper: AnimeDetailMapper,
    private val moshi: Moshi,
    private val crashingReportService: CrashingReportService
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
     * Gets detail information about the currently selected anime and maps it to the respective entity depending on the status code returned.
     * @param malId is the id of the selected anime resouce
     */

    override fun getAnimeDetail(malId: Int): Single<AnimeDetailResult> {
        return jikanService.getDetailAnime(malId.toString()).map { responseBody ->
            when (responseBody.code()) {
                HttpURLConnection.HTTP_OK -> {

                    try {
                        val successResult = AnimeDetailResponseJsonAdapter(moshi).fromJson(
                            requireNotNull(responseBody.body()?.string())
                        )

                        successResult?.let { animeDetailResponse: AnimeDetailResponse ->
                            val animeDetailEntity =
                                animeDetailMapper.tranformAnimeDetailToAnimeEntity(
                                    animeDetailResponse
                                )
                            AnimeDetailResult.Success(animeDetailEntity)
                        }
                    } catch (ex: Exception) {
                        crashingReportService.logException(ex)
                        AnimeDetailResult.Exception("Error occured.", Throwable(ex))
                    }
                }
                else -> {
                    try {
                        val errorResult = JikanErrorResponeJsonAdapter(moshi).fromJson(
                            requireNotNull(responseBody.errorBody()).string()
                        )
                        errorResult?.let {
                            AnimeDetailResult.APIerror(it)
                        }
                    } catch (ex: Exception) {
                        crashingReportService.logException(ex)
                        AnimeDetailResult.Exception("Error occured.", Throwable(ex))
                    }
                }
            }
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
                        AnimeListResult.Success(animeEnities)
                    }
                } catch (ex: Exception) {
                    crashingReportService.logException(ex)
                    AnimeListResult.Exception("Error occured.", Throwable(ex))
                }


            }

            else -> {
                try {
                    val errorResult = JikanErrorResponeJsonAdapter(moshi).fromJson(
                        requireNotNull(responseBody.errorBody()).string()
                    )
                    errorResult?.let {
                        AnimeListResult.APIerror(it)
                    }
                } catch (ex: Exception) {
                    crashingReportService.logException(ex)
                    AnimeListResult.Exception("Error occured.", Throwable(ex))
                }

            }
        }
    }
}