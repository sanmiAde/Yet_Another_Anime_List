package com.sanmidev.yetanotheranimelist.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sanmidev.yetanotheranimelist.data.local.dao.FavouriteAnimeDao
import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.local.model.favourite.FavouriteAnimeEntity

@Database(entities = [FavouriteAnimeEntity::class, AnimeEntity::class], version = 1)
abstract class FavouriteAnimeDatabase : RoomDatabase() {

    abstract fun favouriteDao() : FavouriteAnimeDao
}