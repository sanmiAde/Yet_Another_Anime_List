package com.sanmidev.yetanotheranimelist.di.component

import com.sanmidev.yetanotheranimelist.di.scope.ActivityScope
import com.sanmidev.yetanotheranimelist.ui.airingAnimes.AiringFragment
import com.sanmidev.yetanotheranimelist.ui.upComingAnimes.UpComingAnimesFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface ActivityComponent{
    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }


    fun inject(upComingAnimesFragment: UpComingAnimesFragment)
    fun inject(upComingAnimesFragment: AiringFragment) {

    }
}