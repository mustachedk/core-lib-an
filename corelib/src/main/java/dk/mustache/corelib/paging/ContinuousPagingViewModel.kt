package dk.mustache.corelib.paging

import androidx.lifecycle.ViewModel
import dk.mustache.corelib.paging.GenericPagingAdapter.PagingAdapterItem
import dk.mustache.corelib.paging.Pager.PagingResponse
import io.reactivex.rxjava3.core.Observable

/**
 * Used with a [GenericPagingAdapter] to load data from a data source
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
open class ContinuousPagingViewModel<
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
    protected lateinit var loader: ContinuousPagingAdapterDataLoader<R, P>
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
    open fun hookupLoader(adapter: GenericPagingAdapter<P>) {
        hookupLoader(adapter::createLoadingItems, adapter::addItems)
    }

    /**
     * Hook up the dataloader that loads data from the data source to
     * the adapter.
     *
     * Override to customize creation of the [loader].
     *
     * @param totalPagesCallback Function which consumes the value from
     *     the data source for the total number of
     *     pages. This is [createLoadingItems] for
     *     an unextended [GenericPagingAdapter].
     * @param onNextCallback Function which consumes the
     *     [PagingAdapterItem]s returned by the data source. This
     *     is [addItems] for an unextended [GenericPagingAdapter].
     */
    open fun hookupLoader(
        totalPagesCallback: (totalItemCount: Int) -> Unit,
        onNextCallback: (i: List<P>) -> Unit
    ) {
        loader = ContinuousPagingAdapterDataLoader(pager, totalPagesCallback, onNextCallback)
    }

    /**
     * Override to implement custom mapping between [R]
     * and [P].
     */
    @Suppress("UNCHECKED_CAST")
    open fun mapResponseToPagingItems(response: R): List<P> {
        return response.data as List<P>
    }

    /**
     * Start loading data from the data source and provide data to the
     * functions hooked up with [hookupLoader].
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
}
