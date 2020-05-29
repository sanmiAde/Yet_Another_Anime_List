package com.sanmidev.yetanotheranimelist.utils

import android.app.Application
import com.sanmidev.yetanotheranimelist.utils.NetworkUtils.hasNetwork
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkCacheInterceptor @Inject constructor (val application: Application) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        // Get the request from the chain.
        var request = chain.request()


        /*
        *  Leveraging the advantage of using Kotlin,
        *  we initialize the request and change its header depending on whether
        *  the device is connected to Internet or not.
        */
        request = if (hasNetwork(application)!!)
        /*
        *  If there is Internet, get the cache that was stored 5 seconds ago.
        *  If the cache is older than 5 seconds, then discard it,
        *  and indicate an error in fetching the response.
        *  The 'max-age' attribute is responsible for this behavior.
        */
            request.newBuilder().header("Cache-Control", "public, max-age=" + 60)
                .removeHeader("Pragma").build()
        else
        /*
        *  If there is no Internet, get the cache that was stored 7 days ago.
        *  If the cache is older than 7 days, then discard it,
        *  and indicate an error in fetching the response.
        *  The 'max-stale' attribute is responsible for this behavior.
        *  The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
        */
            request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                .removeHeader("Pragma").build()
        // End of if-else statement

        // Add the modified request to the chain.
        return chain.proceed(request)
    }
}