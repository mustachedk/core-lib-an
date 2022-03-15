package dk.mustache.corelib.paging

import androidx.lifecycle.ViewModel
import dk.mustache.corelib.paging.GenericPagingAdapter.PagingAdapterItem
import dk.mustache.corelib.paging.Pager.PagingResponse
import io.reactivex.rxjava3.core.Observable

/**
 * Used with a [SteppedPagingAdapter] to load data from a data source
 * [pageSize] items at a time into a recyclingview.
 *
 * @param R Type of the response supplied by the data source.
 * @param P Type of the items consumed by the adapter.
 * @property pageSize Desired size of pages. Default: 10.
 * @property startPage Page to start loading from. Default: 0.
 * @param call Data source to load the [PagingResponse] from. If this
 *     parameter is set to null a call
 *     parameter is expected on [startLoading].
 */
open class SteppedPagingViewModel<
        R : PagingResponse<*>,
        P : PagingAdapterItem
        >(
    call: ((page: Int, pageSize: Int) -> Observable<R>)? = null,
    private val pageSize: Int = 10,
    private val startPage: Int = 0
) : ViewModel() {
    /**
     * If you extend ContinuousPagingViewModel you can access the Pager
     * to add extra [Pager.PagingActionListener]s.
     */
    protected val pager = Pager(::mapResponseToPagingItems)
    private lateinit var loader: SteppedPagingAdapterDataLoader<R, P>
    private lateinit var call: (page: Int, pageSize: Int) -> Observable<R>

    init {
        if (call != null) {
            this.call = call
        }
    }

    /**
     * Defaults function for hooking up the dataLoader that loads
     * data from the data source to the adapter. Look at the other
     * hookupLoader function for more customization options.
     */
    open fun hookupLoader(adapter: SteppedPagingAdapter<P>) {
        hookupLoader(
            adapter::addLoadingItems,
            adapter::addItems,
            adapter::addLastItemObserver
        )
    }

    /**
     * Hook up the dataloader that loads data from the data source to
     * the adapter.
     *
     * Override to customize creation of the [loader].
     *
     * @param itemCountCallback Function which consumes the value from
     *     the data source for the total number of
     *     pages. This is [createLoadingItems] for
     *     an unextended [SteppedPagingAdapter].
     * @param onNextCallback Function which consumes the
     *     [PagingAdapterItem]s returned by the data source. This
     *     is [addItems] for an unextended [SteppedPagingAdapter].
     * @param afterNextCallback Function which informs that new
     *     LoadingItems have been added after consumption of
     *     [onNextCallback]. This is [addLastItemObserver]
     *     for an unextended [SteppedPagingAdapter].
     */
    open fun hookupLoader(
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

    /** Override to implement custom mapping between [R] and [P]. */
    @Suppress("UNCHECKED_CAST")
    open fun mapResponseToPagingItems(response: R): List<P> {
        return response.data as List<P>
    }

    /**
     * Override to define how close to the bottom of the list the user
     * should be before new items are loaded. Keep in mind that this includes
     * any LoadingViews (by default the number of LoadingViews = pageSize).
     */
    open fun offsetForLoadingMoreCells(): Int {
        return pageSize + 2 // number of LoadingViews plus a small offset into the loaded cells
    }

    /**
     * Load the first page from the data source and provide the data to
     * the functions hooked up with [hookupLoader].
     *
     * Override to customize loading data from the [loader].
     *
     * @param call Data source to load the [PagingResponse] from. If
     *     this parameter is set to null a call
     *     parameter is expected on the [constructor].
     */
    open fun startLoading(call: ((page: Int, pageSize: Int) -> Observable<R>)? = null) {
        if (call != null) {
            this.call = call
        }
        loader.startLoading(this.call, startPage, pageSize)
    }

    /**
     * Load another page from the data source provide the data to the
     * functions hooked up with [hookupLoader]. Call [startLoading] for
     * the first page.
     */
    fun continueLoading() {
        loader.loadMore()
    }
}