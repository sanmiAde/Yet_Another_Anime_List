package com.sanmidev.yetanotheranimelist.di.component

import com.sanmidev.yetanotheranimelist.di.scope.ActivityScope
import com.sanmidev.yetanotheranimelist.feature.airingAnimes.AiringFragment
import com.sanmidev.yetanotheranimelist.feature.animeDetail.AnimeDetailFragment
import com.sanmidev.yetanotheranimelist.feature.favourites.FavouriteFragment
import com.sanmidev.yetanotheranimelist.feature.upComingAnimes.UpComingAnimesFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface ActivityComponent{
    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }


    fun inject(upComingAnimesFragment: UpComingAnimesFragment)
    fun inject(upComingAnimesFragment: AiringFragment)

    fun inject(animeDetailFragment: AnimeDetailFragment)
    fun inject(favouriteFragment: FavouriteFragment)
}