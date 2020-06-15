package com.sanmidev.yetanotheranimelist.data.network.repo

import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.lang.Exception
import javax.inject.Inject


class FirebaseImpl @Inject constructor(private val firebaseCrashlytics: FirebaseCrashlytics) : CrashingReportService {

    override fun logException(exception: Exception) {
        firebaseCrashlytics.recordException(exception)
    }

}