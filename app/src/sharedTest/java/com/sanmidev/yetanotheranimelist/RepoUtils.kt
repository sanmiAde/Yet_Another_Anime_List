package com.sanmidev.yetanotheranimelist

import com.sanmidev.yetanotheranimelist.data.network.mapper.AnimeDetailMapper
import com.sanmidev.yetanotheranimelist.data.network.mapper.AnimeListMapper
import com.sanmidev.yetanotheranimelist.data.network.repo.JikanRepositoryImpl
import com.sanmidev.yetanotheranimelist.utils.TestAppScheduler
import okhttp3.mockwebserver.MockWebServer

object RepoUtils {

    fun provideJikanRepository(mockWebServer: MockWebServer): JikanRepositoryImpl {
        val jikanService = NetworkTestUtils.provideJikanservice(mockWebServer)

        val moshi = NetworkTestUtils.moshi

        val animeListMapper = AnimeListMapper();

        val animDetailMapper = AnimeDetailMapper()

        return JikanRepositoryImpl(jikanService, animeListMapper, animDetailMapper, moshi)
    }

    fun ProvideTestScheduler(): TestAppScheduler {
        return TestAppScheduler()
    }
}