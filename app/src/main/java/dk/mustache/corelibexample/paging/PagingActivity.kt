package dk.mustache.corelibexample.paging

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableInt
import androidx.fragment.app.activityViewModels
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
    private val viewModel: PagingViewModel by viewModels()

    private val adapter =
        PagingAdapter(R.layout.paging_demo_passenger_item, R.layout.paging_demo_loading_item)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paging)
        initializeAdapter()

        viewModel.actions.observe(this, ::onAction)
    }

    private fun initializeAdapter() {
        val recyclerView = findViewById<RecyclerView>(R.id.airlines_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.configurePagingListener()
    }

    private fun onAction(action: PagingViewModel.Actions) {
        when(action) {
            is PagingViewModel.Actions.CreateLoadingItems -> adapter.createLoadingItems(action.totalHits)
            is PagingViewModel.Actions.UpDateList -> adapter.replaceLoadingItems(action.items)
        }
    }
}