package dk.mustache.corelibexample.paging

import androidx.lifecycle.MutableLiveData
import dk.mustache.corelib.paging.Pager
import dk.mustache.corelib.paging.ContinuousPagingViewModel
import dk.mustache.corelibexample.testdata.PokemonTestRepo
import dk.mustache.corelibexample.testdata.PokemonTestRepo.PokemonResponse
import io.reactivex.rxjava3.core.Observable

class ExtendedPagingViewModel: ContinuousPagingViewModel<PokemonResponse, PokemonPagingItem>() {

    private val api: PokemonTestRepo = PokemonTestRepo()

    val actions = MutableLiveData<Actions>()
    var totalNumberPages: Int = -1

    init {
        pager.addPagingActionListener(object : Pager.PagingActionListener<PokemonPagingItem>() {
            override fun onTotalPagesAcquired(totalPages: Int) {
                totalNumberPages = totalPages
            }

            override fun onPageDownloaded(pageNumber: Int, items: List<PokemonPagingItem>?) {
                if(pageNumber < 3 && (pageNumber + 1) * 10 < totalNumberPages) {
                    actions.value = Actions.IsLoading
                }
                else {
                    actions.value = Actions.FinishedLoading
                }
            }

            override fun onError(e: Throwable) {
                actions.value = Actions.OnError(e)
            }
        })
    }

    override fun mapResponseToPagingItems(response: PokemonResponse): List<PokemonPagingItem> {
        return response.data.map { PokemonPagingItem(it.id, it.name, it.attack, it.defense) }
    }

    fun startLoading() {
        startLoading(::getData)
    }

    private fun getData(page: Int, pageSize: Int): Observable<PokemonResponse> {
        return api.getDataAsyncRx(page, 8)
    }

    interface Actions {
        object IsLoading: Actions
        object FinishedLoading: Actions
        data class OnError(val e: Throwable): Actions
    }
}