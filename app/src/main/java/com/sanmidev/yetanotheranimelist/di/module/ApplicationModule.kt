package com.sanmidev.yetanotheranimelist.di.module

import com.sanmidev.yetanotheranimelist.data.local.repo.FavouriteAnimeRepository
import com.sanmidev.yetanotheranimelist.data.local.repo.FavouriteAnimeRepostoryImpl
import com.sanmidev.yetanotheranimelist.data.network.repo.JikanRepository
import com.sanmidev.yetanotheranimelist.data.network.repo.JikanRepositoryImpl
import com.sanmidev.yetanotheranimelist.utils.AppScheduler
import com.sanmidev.yetanotheranimelist.utils.RxScheduler
import dagger.Binds
import dagger.Module

@Module
abstract class ApplicationModule {
    @Binds
    abstract fun bindsJikanRepository(jikanRepositoryImpl: JikanRepositoryImpl) : JikanRepository

    @Binds
    abstract fun bindRxScheduler(appScheduler: RxScheduler): AppScheduler

    @Binds
    abstract fun bindsFavouriteAnimeRepository(favouriteAnimeRepostoryImpl: FavouriteAnimeRepostoryImpl) : FavouriteAnimeRepository
}