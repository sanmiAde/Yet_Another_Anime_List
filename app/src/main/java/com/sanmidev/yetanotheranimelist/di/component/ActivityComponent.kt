package com.sanmidev.yetanotheranimelist.di.component

import android.app.Application
import com.sanmidev.yetanotheranimelist.MainActivity
import com.sanmidev.yetanotheranimelist.di.scope.ActivityScope
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface ActivityComponent{
    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }


    fun inject(mainActivity: MainActivity)
}