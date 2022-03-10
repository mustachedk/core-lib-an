package dk.mustache.corelibexample.paging

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dk.mustache.corelib.paging.Pager
import dk.mustache.corelib.paging.SteppedPagingAdapterDataLoader
import dk.mustache.corelibexample.testdata.PokemonTestRepo
import dk.mustache.corelibexample.testdata.PokemonTestRepo.PokemonResponse

class SteppedPagingViewModel: ViewModel() {
    private val api: PokemonTestRepo = PokemonTestRepo()
    protected val pager = Pager(::mapResponseToPagingItems)
    lateinit var loader: SteppedPagingAdapterDataLoader<PokemonResponse, PokemonPagingItem>

    fun hookupLoader(
        itemCountCallback: (totalItemCount: Int) -> Unit,
        onNextCallback: (i: List<PokemonPagingItem>) -> Unit
    ) {
        loader = SteppedPagingAdapterDataLoader(pager, itemCountCallback, onNextCallback)
    }

    private fun mapResponseToPagingItems(response: PokemonResponse): List<PokemonPagingItem> {
        return response.data.map { PokemonPagingItem(it.id, it.name, it.attack, it.defense) }
    }

    fun startLoading() {
        loader.startLoading(api::getDataAsyncRx, 0, 10)
    }

    fun continueLoading() {
        loader.loadMore()
    }
}