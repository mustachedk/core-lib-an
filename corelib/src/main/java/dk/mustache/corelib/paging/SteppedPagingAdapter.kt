package dk.mustache.corelib.paging

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.utils.RecyclerViewCellVisibilityObserver

/**
 * Adapter for loading items in batches, adding new batches to the
 * bottom of the list.
 *
 * Includes support for loading-cells
 * ([GenericPagingAdapter.LoadingItem]s), which are automatically
 * replaced when new items are added (with [addItems] function).
 *
 * Also includes an observer which when started with
 * [addLastItemObserver] will call [onReachEnd] when the user reaches
 * the bottom of the recyclerview defined in [setRecyclerview].
 *
 * @param viewResource Layout for item cells.
 * @param loadingResource Layout for [GenericPagingAdapter.LoadingItem]
 *     cells.
 * @param cellOffsetSource Function which returns an offset for the
 *     [lastItemObserver][addLastItemObserver].
 * @param onReachEnd Function which is called when the user reaches the
 *     bottom of the recyclerview.
 */
open class SteppedPagingAdapter<T : GenericPagingAdapter.PagingAdapterItem>(
    @LayoutRes viewResource: Int,
    @LayoutRes loadingResource: Int,
    private val cellOffsetSource: (() -> Int)? = null,
    private val onReachEnd: () -> Unit
) : GenericPagingAdapter<T>(viewResource, loadingResource) {
    private lateinit var cellObserver: RecyclerViewCellVisibilityObserver

    /**
     * Set the adaper's recyclerview. Needed to use
     * [addLastItemObserver].
     */
    fun setRecyclerview(view: RecyclerView) {
        cellObserver = RecyclerViewCellVisibilityObserver(view)
    }

    /**
     * Start observing the recyclerview. When the user reaches the
     * bottom of the recyclerview (minus any offset defined by
     * [cellOffsetSource]) [onReachEnd] is called and the observer is
     * turned off.
     *
     * Call [addLastItemObserver] again to restart the observer once the
     * recyclerview has been populated with more items. (If you do not
     * populate with more items first the observer will immediately call
     * [onReachEnd] and turn off again, since it will still be at the
     * bottom.)
     */
    fun addLastItemObserver() {
        val offset = cellOffsetSource?.invoke() ?: 0

        cellObserver.observeBottomCellVisible(offset) {
            onReachEnd()
            cellObserver.clearAllObservers()
            // Remove cellObserver so that we don't load more until a page has been loaded
            // and addLastItemObserver is called again
        }
    }
}