package dk.mustache.corelibexample.paging

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dk.mustache.corelib.paging.Paging
import dk.mustache.corelibexample.testdata.PokemonTestRepo
import io.reactivex.rxjava3.core.Observable

class PagingViewModel: ViewModel() {
    private val api: PokemonTestRepo = PokemonTestRepo()
    private val pagingLib = Paging(::map)

    val actions = MutableLiveData<Actions>()

    @SuppressLint("CheckResult")
    fun configurePagingListener() {
        pagingLib.listener = object : Paging.PagingActionListener<PokePagingItem> {
            override fun onPageDownloaded(pageNumber: Int, response: List<PokePagingItem>?) {
                response?.let {
                    actions.value = Actions.UpDateList(it)
                }
            }

            override fun onFinished(pageNumber: Int, response: List<PokePagingItem>?) {
                response?.let {
                    actions.value = Actions.UpDateList(it)
                }
            }

            override fun onError(errorMessage: String) {}

            override fun onCancel() {}

            override fun onTotalPagesAcquired(totalPages: Int) {
                actions.value = Actions.CreateLoadingItems(totalPages)
            }
        }
        performRequest()
    }

    private fun performRequest() {
        pagingLib.cancel()
        pagingLib.pagedRetrofitCall(
            retrofitCall = ::getData, startPage = 0, pageSize = 10, totalNumberOfHits = 0
        )
    }

    private fun getData(page: Int, pageSize: Int): Observable<PokemonTestRepo.PokemonResponse> {
        return api.getDataAsyncRx(page, pageSize)
    }

    private fun map(response: PokemonTestRepo.PokemonResponse): List<PokePagingItem> {
        return response.data.map { PokePagingItem.map(it) }
    }

    interface Actions {
        data class CreateLoadingItems(val totalHits: Int): Actions
        data class UpDateList(val items: List<PokePagingItem>): Actions
    }
}