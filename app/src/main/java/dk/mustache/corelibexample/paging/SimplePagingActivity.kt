package dk.mustache.corelibexample.paging

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.paging.GenericPagingAdapter
import dk.mustache.corelib.paging.ContinuousPagingViewModel
import dk.mustache.corelib.paging.ContinuousPagingViewModelFactory
import dk.mustache.corelibexample.R
import dk.mustache.corelibexample.testdata.PokemonTestRepo
import dk.mustache.corelibexample.testdata.PokemonTestRepo.PokemonItem
import dk.mustache.corelibexample.testdata.PokemonTestRepo.PokemonResponse

class SimplePagingActivity : AppCompatActivity() {
    private val viewModel: ContinuousPagingViewModel<PokemonResponse, PokemonItem> by viewModels {
        val api = PokemonTestRepo()
        ContinuousPagingViewModelFactory<PokemonResponse, PokemonItem>(api::getDataAsyncRx)
    }
    private lateinit var adapter: GenericPagingAdapter<PokemonItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paging)
        initializeAdapter()
        startDataLoading()
    }

    private fun initializeAdapter() {
        adapter = GenericPagingAdapter(
            R.layout.paging_demo_simple_item,
            R.layout.paging_demo_loading_item
        )
        val recyclerView = findViewById<RecyclerView>(R.id.pokemon_recycler)
        recyclerView.adapter = adapter
    }

    private fun startDataLoading() {
        viewModel.hookupLoader(adapter)
        viewModel.startLoading()
    }
}