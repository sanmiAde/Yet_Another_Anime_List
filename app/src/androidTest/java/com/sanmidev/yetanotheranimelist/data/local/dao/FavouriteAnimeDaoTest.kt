package com.sanmidev.yetanotheranimelist.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import com.sanmidev.yetanotheranimelist.data.local.db.FavouriteAnimeDatabase
import com.sanmidev.yetanotheranimelist.data.local.model.FavouriteAnimeEntity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavouriteAnimeDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var db: FavouriteAnimeDatabase? = null

    private var favouriteAnimeDao: FavouriteAnimeDao? = null

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            FavouriteAnimeDatabase::class.java
        ).build()

        favouriteAnimeDao = db!!.favouriteDao()
    }


    @Test
    fun favouriteAnime_shouldSaveAnimeDetail_when_animeIsFavourited() {
        //GIVEN
//        val favouriteAnime1 = FavouriteAnimeEntity(234, true)
//
//        //WHEN
//        favouriteAnimeDao!!.favouriteAnime(favouriteAnime1)
//
//        //THEN
//        val animes = favouriteAnimeDao!!.getFavouriteAnimes()
//
//        Truth.assertThat(animes).isGreaterThan(0)

    }


    @After
    fun tearDown() {
        db!!.close()
    }
}