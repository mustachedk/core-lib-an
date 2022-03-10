package dk.mustache.corelib.paging

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable

open class ContinuousPagingViewModel<
        R : Pager.PagingResponse<*>,
        P : GenericPagingAdapter.PagingAdapterItem
        >(
    call: ((page: Int, pageSize: Int) -> Observable<R>)? = null,
    private val pageSize: Int = 10,
    private val startPage: Int = 0
) : ViewModel() {
    protected val pager = Pager(::mapResponseToPagingItems)
    protected lateinit var loader: ContinuousPagingAdapterDataLoader<R, P>
    private lateinit var call: (page: Int, pageSize: Int) -> Observable<R>

    init {
        if(call != null) {
            this.call = call
        }
    }

    open fun hookupLoader(
        itemCountCallback: (totalItemCount: Int) -> Unit,
        onNextCallback: (i: List<P>) -> Unit
    ) {
        loader = ContinuousPagingAdapterDataLoader(pager, itemCountCallback, onNextCallback)
    }

    open fun mapResponseToPagingItems(response: R): List<P> {
        return response.data as List<P>
    }

    open fun startLoading(call: ((page: Int, pageSize: Int) -> Observable<R>)? = null) {
        if(call != null) {
            this.call = call
        }
        loader.startLoading(this.call, startPage, pageSize)
    }
}
