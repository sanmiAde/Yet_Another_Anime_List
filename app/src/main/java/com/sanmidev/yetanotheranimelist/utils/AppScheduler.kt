package com.sanmidev.yetanotheranimelist.utils

import com.sanmidev.yetanotheranimelist.di.scope.ApplicationScope
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
@ApplicationScope
class AppScheduler @Inject constructor()  : RxScheduler{
    override fun io() = Schedulers.io()
    override fun main(): Scheduler = AndroidSchedulers.mainThread()
}

class  TestAppScheduler() : RxScheduler{

    override fun io(): Scheduler = Schedulers.trampoline()

    override fun main(): Scheduler = Schedulers.trampoline()
}

interface RxScheduler {
    fun io(): Scheduler

    fun main(): Scheduler
}