package dk.mustache.corelibexample.paging

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.paging.GenericPagingAdapter
import dk.mustache.corelib.paging.ContinuousPagingViewModel
import dk.mustache.corelib.paging.PagingViewModelFactory
import dk.mustache.corelibexample.R
import dk.mustache.corelibexample.testdata.PokemonTestRepo
import dk.mustache.corelibexample.testdata.PokemonTestRepo.PokemonItem
import dk.mustache.corelibexample.testdata.PokemonTestRepo.PokemonResponse

class SimplePagingActivity : AppCompatActivity() {
    private val viewModel: ContinuousPagingViewModel<PokemonResponse, PokemonItem> by viewModels {
        val api = PokemonTestRepo()
        PagingViewModelFactory<PokemonResponse, PokemonItem>(api::getDataAsyncRx)
    }
    lateinit var adapter: GenericPagingAdapter<PokemonItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paging)
    }

    override fun onResume() {
        super.onResume()
        initializeAdapter()
        viewModel.startLoading()
    }

    private fun initializeAdapter() {
        adapter = GenericPagingAdapter(
            R.layout.paging_demo_simple_item,
            R.layout.paging_demo_loading_item
        )
        val recyclerView = findViewById<RecyclerView>(R.id.pokemon_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        viewModel.hookupLoader(adapter::createLoadingItems, adapter::replaceLoadingItems)
    }
}