package com.sanmidev.yetanotheranimelist.data.network.repo

import java.lang.Exception

/***
 * Repository for interacting with a software as a service platform
 */
interface Saas {

    fun logException(exception: Exception)
}