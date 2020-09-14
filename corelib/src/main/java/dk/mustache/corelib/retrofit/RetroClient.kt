package dk.mustache.corelib.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** instantiates singleton retroFitClient with GSON parser **/

object RetroClient {
    fun getRetrofitInstance(baseUrl: String): Retrofit {
        val retroFitInstance by lazy {
            Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(
                GsonConverterFactory.create()
            ).build()
        }
        return retroFitInstance
    }
}