package com.sanmidev.yetanotheranimelist.di.module

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.sanmidev.yetanotheranimelist.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {

    @ApplicationScope
    @Provides
    fun providesFirebaseCrashlytics() : FirebaseCrashlytics{
       return FirebaseCrashlytics.getInstance()
    }
}