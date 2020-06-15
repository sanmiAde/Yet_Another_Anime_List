package com.sanmidev.yetanotheranimelist.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_anime_table")
class FavouriteAnimeEntity(
    @PrimaryKey
    val malId : Int,

    val isFavourite : Boolean = false
)