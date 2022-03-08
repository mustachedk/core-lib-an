package dk.mustache.corelibexample.paging

import androidx.lifecycle.ViewModel
import dk.mustache.corelib.paging.Paging
import dk.mustache.corelibexample.R
import dk.mustache.corelibexample.testdata.PokemonTestRepo

class PagingViewModel: ViewModel() {
    private val api: PokemonTestRepo = PokemonTestRepo()

    private val paging = Paging(::mapper)
    val adapter = PagingAdapter(
        R.layout.paging_demo_passenger_item,
        R.layout.paging_demo_loading_item,
        paging
    )

    fun loadData() {
        paging.cancel()
        paging.pagedRetrofitCall(
            retrofitCall = api::getDataAsyncRx, startPage = 0, pageSize = 10, totalNumberOfHits = 0
        )
    }

    private fun mapper(response: PokemonTestRepo.PokemonResponse): List<PokePagingItem> {
        return response.data.map { PokePagingItem.map(it) }
    }
}