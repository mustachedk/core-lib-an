package dk.mustache.corelib.paging

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable

open class SteppedPagingViewModel<
        R : Pager.PagingResponse<*>,
        P : GenericPagingAdapter.PagingAdapterItem
        >(
    private val call: ((page: Int, pageSize: Int) -> Observable<R>)? = null,
    private val pageSize: Int = 10,
    private val startPage: Int = 0
) : ViewModel() {
    protected val pager = Pager(::mapResponseToPagingItems)
    private lateinit var loader: SteppedPagingAdapterDataLoader<R, P>

    fun hookupLoader(
        itemCountCallback: (totalItemCount: Int) -> Unit,
        onNextCallback: (i: List<P>) -> Unit,
        afterNextCallback: () -> Unit
    ) {
        loader = SteppedPagingAdapterDataLoader(
            pager,
            itemCountCallback,
            onNextCallback,
            { afterNextCallback() }
        )
    }

    @Suppress("UNCHECKED_CAST")
    open fun mapResponseToPagingItems(response: R): List<P> {
        return response.data as List<P>
    }

    open fun offsetForLoadingMoreCells(): Int {
        return pageSize + 2 // number of loadingCells plus a small offset into the loaded cells
    }

    fun startLoading() {
        loader.startLoading(call, startPage, pageSize)
    }

    fun continueLoading() {
        loader.loadMore()
    }
}