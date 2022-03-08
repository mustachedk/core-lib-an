package dk.mustache.corelibexample.paging
//
//import android.annotation.SuppressLint
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import dk.mustache.corelib.paging.Paging
//import dk.mustache.corelibexample.testdata.PokemonTestRepo
//import io.reactivex.rxjava3.core.Observable
//
//class PagingViewModel: ViewModel() {
//    private val api: PokemonTestRepo = PokemonTestRepo()
//    private val pagingLib = Paging<PokePagingItem>()
//
//    val actions = MutableLiveData<Actions>()
//
//    @SuppressLint("CheckResult")
//    fun configurePagingListener() {
//        pagingLib.listener = object : Paging.PagingActionListener<PokemonTestRepo.PokemonResponse> {
//            override fun onTotalHitsResolved(response: PokemonTestRepo.PokemonResponse): Int {
//                val totalHits = response.totalPages.toInt()
//                actions.value = Actions.CreateLoadingItems(totalHits)
//                return totalHits
//            }
//
//            override fun onPageDownloaded(pageNumber: Int, response: PokemonTestRepo.PokemonResponse?) {
//                response?.items?.let {
//                    actions.value = Actions.UpDateList(it.map { PokePagingItem.map(it) })
//                }
//            }
//
//            override fun onFinished(pageNumber: Int, response: PokemonTestRepo.PokemonResponse?) {
//                response?.items?.let {
//                    actions.value = Actions.UpDateList(it.map { PokePagingItem.map(it) })
//                }
//            }
//
//            override fun onError(errorMessage: String) {}
//
//            override fun onCancel() {}
//        }
//        performRequest()
//    }
//
//    private fun performRequest() {
//        pagingLib.cancel()
//        pagingLib.pagedRetrofitCall(
//            retrofitCall = ::getData, startPage = 0, pageSize = 10, totalNumberOfHits = 0
//        )
//    }
//
//    private fun getData(page: Int, pageSize: Int): Observable<PokemonTestRepo.PokemonResponse> {
//        return api.getDataAsyncRx(page, pageSize)
//    }
//
//    interface Actions {
//        data class CreateLoadingItems(val totalHits: Int): Actions
//        data class UpDateList(val items: List<PokePagingItem>): Actions
//    }
//}