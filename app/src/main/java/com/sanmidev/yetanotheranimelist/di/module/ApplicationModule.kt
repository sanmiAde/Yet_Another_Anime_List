package com.sanmidev.yetanotheranimelist.di.module

import com.sanmidev.yetanotheranimelist.data.network.repo.FirebaseImpl
import com.sanmidev.yetanotheranimelist.data.network.repo.JikanRepository
import com.sanmidev.yetanotheranimelist.data.network.repo.JikanRepositoryImpl
import com.sanmidev.yetanotheranimelist.data.network.repo.Saas
import com.sanmidev.yetanotheranimelist.utils.AppScheduler
import com.sanmidev.yetanotheranimelist.utils.RxScheduler
import dagger.Binds
import dagger.Module

@Module
abstract class ApplicationModule {
    @Binds
    abstract fun bindsJikanRepository(jikanRepositoryImpl: JikanRepositoryImpl) : JikanRepository

    @Binds
    abstract fun bindRxScheduler(appScheduler: AppScheduler) : RxScheduler

    @Binds
    abstract fun bindsSAAS(firebaseImpl: FirebaseImpl) : Saas
}