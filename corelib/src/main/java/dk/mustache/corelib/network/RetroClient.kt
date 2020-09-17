package dk.mustache.corelib.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/** Singleton retroFitClient with
 * - GSON parser
 * - OkHttpTokenRefreshAuthenticator which signs request with a new access token if needed
 * - OkHttpAuthorizationInterceptor which automatically retries services failed with missing auth/401
 * **/

object RetroClient {
    var retrofitInstance : Retrofit? = null

    fun <T> buildRetrofitInstance(
        baseUrl: String,
        apiClass: Class<T>,
        authorizationRepository: AuthorizationRepository
    ) {
        retrofitInstance  = Retrofit.Builder().baseUrl(baseUrl)
            .client(getOkHttpClient(authorizationRepository))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create()
            ).build()
    }

    private fun getOkHttpClient(authorizationRepository: AuthorizationRepository): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(OkHttpAuthorizationInterceptor(authorizationRepository))
            .authenticator(OkHttpTokenRefreshAuthenticator(authorizationRepository))
            .build()

    fun <T> getRetrofitInstance(
        baseUrl: String,
        apiClass: Class<T>,
        authorizationRepository: AuthorizationRepository
    ) {
        retrofitInstance  = Retrofit.Builder().baseUrl(baseUrl)
            .client(getOkHttpClient(authorizationRepository))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create()
            ).build()
    }
}