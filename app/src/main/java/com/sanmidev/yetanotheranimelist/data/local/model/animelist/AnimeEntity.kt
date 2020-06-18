package com.sanmidev.yetanotheranimelist.data.local.model.animelist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime_entity_table")
data class AnimeEntity(
    val imageUrl: String,
    @PrimaryKey
    val id: Int,
    val title: String
)
