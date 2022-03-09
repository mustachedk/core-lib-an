package dk.mustache.corelibexample.paging

import androidx.lifecycle.ViewModel
import dk.mustache.corelib.paging.Paging
import dk.mustache.corelibexample.R
import dk.mustache.corelibexample.testdata.PokemonTestRepo

class PagingViewModel: ViewModel() {
    private val api: PokemonTestRepo = PokemonTestRepo()

    val adapter = PagingAdapter(
        R.layout.paging_demo_loaded_item,
        R.layout.paging_demo_loading_item,
        Paging()
    )

    fun loadData() {
        adapter.startLoading(api::getDataAsyncRx)
    }
}