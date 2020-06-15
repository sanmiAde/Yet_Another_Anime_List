package com.sanmidev.yetanotheranimelist.di.module

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sanmidev.yetanotheranimelist.data.local.dao.FavouriteAnimeDao
import com.sanmidev.yetanotheranimelist.data.local.db.FavouriteAnimeDatabase
import com.sanmidev.yetanotheranimelist.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class RoomModule {
//
    @Provides
    @ApplicationScope
    fun providesRoomDatabase(application: Application): FavouriteAnimeDatabase {
        return Room.databaseBuilder(application, FavouriteAnimeDatabase::class.java, DBNAME).build()
    }


    @Provides
    @ApplicationScope
    fun  providesFavouriteDao(db : FavouriteAnimeDatabase): FavouriteAnimeDao {
        return db.favouriteDao()
    }

    companion object{
     const val   DBNAME = "Favourite_Database"
    }
}