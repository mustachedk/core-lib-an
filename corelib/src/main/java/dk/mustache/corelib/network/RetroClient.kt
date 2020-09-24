package dk.mustache.corelib.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/** Singleton retroFitClient with
 * - GSON parser
 * - OkHttpTokenRefreshAuthenticator which signs request with a new access token if needed
 * - OkHttpAuthorizationInterceptor which automatically retries services failed with missing auth/401
 * **/

object RetroClient  {
    lateinit var webApi : Any
    fun <T> buildWebApi(
        baseUrl: String,
        apiClass: Class<T>,
        authorizationRepository: AuthorizationRepository
    ) : T {
        webApi = Retrofit.Builder().baseUrl(baseUrl)
            .client(getOkHttpClient(authorizationRepository))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create()
            ).build().create(apiClass) as Any

        return webApi as T
    }

    private fun getOkHttpClient(authorizationRepository: AuthorizationRepository): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(OkHttpAuthorizationInterceptor(authorizationRepository))
            .authenticator(OkHttpTokenRefreshAuthenticator(authorizationRepository))
            .build()
}