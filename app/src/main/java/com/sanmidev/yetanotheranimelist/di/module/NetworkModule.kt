package com.sanmidev.yetanotheranimelist.di.module

import android.app.Application
import com.sanmidev.yetanotheranimelist.BuildConfig
import com.sanmidev.yetanotheranimelist.data.network.service.JikanService
import com.sanmidev.yetanotheranimelist.di.scope.ApplicationScope
import com.sanmidev.yetanotheranimelist.utils.NetworkCacheInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    @ApplicationScope
    fun providesOkHTTPCache(application: Application) : Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }


    @Provides
    @ApplicationScope
    fun providesOkHTTPLoggingInterceptor() : HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()

       if(BuildConfig.BUILD_TYPE != "release" ){
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }else{
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        return loggingInterceptor
    }


    @Provides
    @ApplicationScope
    fun providesOkHTTPClient(cache: Cache, httpLoggingInterceptor: HttpLoggingInterceptor, networkCacheInterceptor: NetworkCacheInterceptor) : OkHttpClient{
        return  OkHttpClient.Builder()
            .addInterceptor(networkCacheInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .cache(cache)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS).build()
    }

    @Provides
    @ApplicationScope
    fun providesMoshi() : Moshi {
        return  Moshi.Builder().add((KotlinJsonAdapterFactory()))
            .build()

    }

    @Provides
    @ApplicationScope
    fun provideRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @ApplicationScope
    fun providesJikanRetrofit( retrofit: Retrofit) : JikanService {
        return retrofit.create(JikanService::class.java)
    }

}