package dk.mustache.corelib.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/** Interceptor which adds auth token to all RetroFit services. If a service fails, the interceptor will try to re-authenticate and automatically retry the failed service**/

class OkHttpAuthorizationInterceptor(
    private val authorizationRepository: AuthorizationRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().signedRequest()
        return chain.proceed(newRequest)
    }


    private fun Request.signedRequest() = when (AuthorizationType.fromRequest(this)) {
        AuthorizationType.ACCESS_TOKEN -> signWithFreshAccessToken(newBuilder())
//      AuthorizationType.CLIENT_CREDENTIALS -> signWithClientCredentialsToken(newBuilder())
        AuthorizationType.NONE -> this
        else -> this
    }

    private fun signWithClientCredentialsToken(newBuilder: Request.Builder) {}

    private fun signWithFreshAccessToken(newBuilder: Request.Builder): Request {
        val accessToken = authorizationRepository.fetchFreshAccessToken()
        return newBuilder
            .header("Authorization", "Bearer ${accessToken.rawToken}")
            .build()
    }
}

interface AuthorizationRepository {
    /** return the stored access token if it hasn’t expired yet or obtain a new one if it has.
     * When token is received, it is immediately used to retry the last failed service.
     * Thus this this method must be synchronous. RetroFit has a synchronous execute function which can be used. **/
    fun fetchFreshAccessToken(): AccessToken
}
