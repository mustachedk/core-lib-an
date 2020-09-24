package dk.mustache.corelib.network

import android.util.Log
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class OkHttpTokenRefreshAuthenticator(
    private val authorizationRepository: AuthorizationRepository
) : Authenticator {
    private val MAX_RETRIES: Int = 2

    override fun authenticate(route: Route?, response: Response): Request? = when {
        response.retryCount > MAX_RETRIES -> null
        else -> response.createSignedRequest()
    }

    private fun Response.createSignedRequest(): Request? = try {
        val accessToken = authorizationRepository.fetchFreshAccessToken()
        //TODO reintroduce okhttp:4.9.0 later - breaks meny/spar
        request()
        //request.signWithToken(accessToken)
    } catch (error: Throwable) {
        Log.e("OkHttp", "createSignedRequest: failed")
        null
    }

    private fun Request.signWithToken(accessToken: AccessToken) =
        newBuilder()
            .header("Authorization", "Bearer ${accessToken.rawToken}")
            .build()

    private val Response.retryCount: Int
        get() {
//            var currentResponse = priorResponse
            var result = 0
//            while (currentResponse != null) {
//                result++
//                currentResponse = currentResponse.priorResponse
//            }
            return result
        }
}