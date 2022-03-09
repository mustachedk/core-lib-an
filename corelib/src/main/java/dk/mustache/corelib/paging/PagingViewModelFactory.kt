package dk.mustache.corelib.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxjava3.core.Observable

class PagingViewModelFactory<
        R : Pager.PagingResponse<*>,
        P : GenericPagingAdapter.PagingAdapterItem
        >(
    private val call: (page: Int, pageSize: Int) -> Observable<R>,
    private val pageSize: Int = 10,
    private val startPage: Int = 0
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ContinuousPagingViewModel<R, P>(call, pageSize, startPage) as T
    }
}