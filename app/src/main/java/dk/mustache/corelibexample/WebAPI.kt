package dk.mustache.corelibexample

import dk.mustache.corelib.network.AuthorizationType
import dk.mustache.corelibexample.model.MockResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Tag

class WebAPI() {
    public interface MockService {
        @GET("/posts/1")
        fun getMockDB(@Tag authorization: AuthorizationType = AuthorizationType.CLIENT_CREDENTIALS): Observable<MockResponse>
    }
}