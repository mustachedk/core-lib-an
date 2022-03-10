package dk.mustache.corelibexample.paging

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.paging.SteppedPagingAdapter
import dk.mustache.corelib.paging.SteppedPagingViewModel
import dk.mustache.corelib.paging.SteppedPagingViewModelFactory
import dk.mustache.corelibexample.R
import dk.mustache.corelibexample.testdata.PokemonTestRepo
import dk.mustache.corelibexample.testdata.PokemonTestRepo.PokemonItem
import dk.mustache.corelibexample.testdata.PokemonTestRepo.PokemonResponse

class SteppedPagingActivity : AppCompatActivity() {
    private val viewModel: SteppedPagingViewModel<PokemonResponse, PokemonItem> by viewModels {
        val api = PokemonTestRepo()
        SteppedPagingViewModelFactory<PokemonResponse, PokemonItem>(api::getDataAsyncRx, 6)
    }
    private lateinit var adapter: SteppedPagingAdapter<PokemonItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paging)
        initializeAdapter()
        viewModel.startLoading()
    }

    private fun initializeAdapter() {
        adapter = SteppedPagingAdapter(
            R.layout.paging_demo_simple_item,
            R.layout.paging_demo_loading_item,
            viewModel::offsetForLoadingMoreCells
        ) {
            viewModel.continueLoading()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.pokemon_recycler)
        adapter.setRecyclerview(recyclerView)
        recyclerView.adapter = adapter
        viewModel.hookupLoader(
            adapter::addLoadingItems,
            adapter::replaceLoadingItems,
            adapter::addLastItemObserver
        )
    }
}