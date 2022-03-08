package dk.mustache.corelibexample.paging.network

import dk.test.pagingadaptertest.network.PassengersResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {
    @GET("passenger")
    fun fetchPage(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Observable<PassengersResponse>
}
