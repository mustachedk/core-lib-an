package dk.mustache.corelibexample

import dk.mustache.corelib.network.AuthorizationType
import dk.mustache.corelibexample.model.MockResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Tag

class WebAPI() {
    public interface MockService {
        @GET("/posts/1")
        fun getMockDB(@Tag authorization: AuthorizationType = AuthorizationType.CLIENT_CREDENTIALS) : Call<MockResponse>
    }
}