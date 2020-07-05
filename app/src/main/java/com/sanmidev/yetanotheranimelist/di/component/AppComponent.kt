package com.sanmidev.yetanotheranimelist.di.component

import android.app.Application
import com.sanmidev.yetanotheranimelist.di.module.ApplicationModule
import com.sanmidev.yetanotheranimelist.di.module.AssistedInjectModule
import com.sanmidev.yetanotheranimelist.di.module.NetworkModule
import com.sanmidev.yetanotheranimelist.di.module.RoomModule
import com.sanmidev.yetanotheranimelist.di.scope.ApplicationScope
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [ApplicationModule::class, NetworkModule::class, AssistedInjectModule::class, RoomModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application) : AppComponent
    }

    fun activityComponent() : ActivityComponent.Factory
}