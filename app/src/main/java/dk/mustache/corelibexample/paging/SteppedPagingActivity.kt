package dk.mustache.corelibexample.paging

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.paging.GenericPagingAdapter
import dk.mustache.corelib.utils.RecyclerViewCellVisibilityObserver
import dk.mustache.corelibexample.R

class SteppedPagingActivity : AppCompatActivity() {
    private val viewModel: SteppedPagingViewModel by viewModels()
    lateinit var adapter: GenericPagingAdapter<PokemonPagingItem>
    lateinit var cellObserver: RecyclerViewCellVisibilityObserver

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
            R.layout.paging_demo_loaded_item,
            R.layout.paging_demo_loading_item
        )
        val recyclerView = findViewById<RecyclerView>(R.id.pokemon_recycler)
        recyclerView.adapter = adapter
        viewModel.hookupLoader(adapter::addLoadingItems, adapter::replaceLoadingItems)

        cellObserver = RecyclerViewCellVisibilityObserver(recyclerView)

        viewModel.loader.update.observe(this) { lastPageLoaded ->
                Log.wtf("test", "Page Loaded: $lastPageLoaded")
                observeCell(lastPageLoaded)
        }
    }

    private fun observeCell(lastPageLoaded: Int) {

        val thirdtoLastCell = (lastPageLoaded + 1) * 10 - 4
        Log.wtf("test", "Observing: $thirdtoLastCell")
        cellObserver.observeLastVisibleCellIsPast(thirdtoLastCell) { pos ->
            viewModel.continueLoading()
            cellObserver.clearAllObservers()
        }
    }
}