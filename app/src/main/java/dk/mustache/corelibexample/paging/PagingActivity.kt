package dk.mustache.corelibexample.paging

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.paging.Paging
import dk.mustache.corelibexample.R
import dk.mustache.corelibexample.paging.network.RetrofitApi
import dk.mustache.corelibexample.paging.network.RetrofitClient
import dk.mustache.corelibexample.testdata.PokemonTestRepo
import dk.test.pagingadaptertest.network.Passenger
import dk.test.pagingadaptertest.network.PassengersResponse

const val BASE_URL: String = "https://api.instantwebtools.net/v1/"

class PagingActivity : AppCompatActivity() {
    private val api: RetrofitApi = RetrofitClient().createRetrofitClient(BASE_URL)
    private val pagingLib = Paging<PassengersResponse>()
    private val adapter =
        PagingAdapter(R.layout.paging_demo_passenger_item, R.layout.paging_demo_loading_item)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paging)
        initializeAdapter()
    }

    private fun initializeAdapter() {
        val recyclerView = findViewById<RecyclerView>(R.id.airlines_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        configurePagingListener()
    }

    @SuppressLint("CheckResult")
    private fun configurePagingListener() {
        pagingLib.listener = object : Paging.PagingActionListener<PassengersResponse> {
            override fun onTotalHitsResolved(response: PassengersResponse): Int {
                val totalHits = response.totalPages.toInt()
                adapter.createLoadingItems(totalHits)
                return totalHits
            }

            override fun onPageDownloaded(pageNumber: Int, response: PassengersResponse?) {
                response?.data?.let {
                    it.forEach { it.page = pageNumber }
                    updateList(it)
                }
            }

            override fun onFinished(pageNumber: Int, response: PassengersResponse?) {
                response?.data?.let {
                    it.forEach { it.page = pageNumber }
                    updateList(it)
                }
            }

            override fun onError(errorMessage: String) {}

            override fun onCancel() {}
        }
        performRequest()
    }

    private fun performRequest() {
        pagingLib.cancel()
        pagingLib.pagedRetrofitCall(
            retrofitCall = api::fetchPage, startPage = 0, pageSize = 10, totalNumberOfHits = 0
        )
    }

    private fun updateList(list: List<Passenger>) {
        adapter.replaceLoadingItems(list)
    }
}