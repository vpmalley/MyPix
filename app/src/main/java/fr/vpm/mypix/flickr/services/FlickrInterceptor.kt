package fr.vpm.mypix.flickr.services

import android.content.Context
import fr.vpm.mypix.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by vince on 31/03/18.
 */
class FlickrInterceptor(private val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val urlWithApiKey = request.url().newBuilder()
                .addEncodedQueryParameter("format", "json")
                .addEncodedQueryParameter("nojsoncallback", "1")
                .addEncodedQueryParameter("api_key", BuildConfig.FLICKR_API_KEY)
                .build()
        val newRequest = request.newBuilder().url(urlWithApiKey).build()
        return chain.proceed(newRequest)
    }

}