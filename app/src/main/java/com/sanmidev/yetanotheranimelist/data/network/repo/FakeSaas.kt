package com.sanmidev.yetanotheranimelist.data.network.repo

import java.lang.Exception

/***
 * Fake Saas impl for testing
 */
class FakeSaas : Saas{
    override fun logException(exception: Exception) {
        println(exception)
    }

}