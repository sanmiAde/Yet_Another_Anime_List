package com.sanmidev.yetanotheranimelist.di.component

import com.sanmidev.yetanotheranimelist.di.scope.ActivityScope
import com.sanmidev.yetanotheranimelist.presentation.upComingAnimes.UpComingAnimesFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface ActivityComponent{
    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }


    fun inject(mainActivity: UpComingAnimesFragment)
}