package dk.mustache.corelibexample

import dk.mustache.corelibexample.model.MockResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

class WebAPI() {
    public interface MockService {
        @GET("/posts/1")
        fun getMockDB() : Call<MockResponse>
    }
}