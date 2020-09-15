package dk.mustache.corelib.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Singleton retroFitClient with
 * - GSON parser
 * - OkHttpTokenRefreshAuthenticator which signs request with a new access token if needed
 * - OkHttpAuthorizationInterceptor which automatically retries services failed with missing auth/401
 * **/

object RetroClient {
    fun getRetrofitInstance(
        baseUrl: String,
        authorizationRepository: AuthorizationRepository
    ): Retrofit {
        val retroFitInstance by lazy {
            Retrofit.Builder().baseUrl(baseUrl)
                .client(getOkHttpClient(authorizationRepository))
                .addConverterFactory(
                    GsonConverterFactory.create()
                ).build()
        }
        return retroFitInstance
    }

    private fun getOkHttpClient(authorizationRepository: AuthorizationRepository): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(OkHttpAuthorizationInterceptor(authorizationRepository))
            .authenticator(OkHttpTokenRefreshAuthenticator(authorizationRepository))
            .build()
}