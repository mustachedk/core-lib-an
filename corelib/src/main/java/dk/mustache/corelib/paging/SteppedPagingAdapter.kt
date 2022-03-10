package dk.mustache.corelib.paging

import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.utils.RecyclerViewCellVisibilityObserver

class SteppedPagingAdapter<T : GenericPagingAdapter.PagingAdapterItem>(
    viewResource: Int,
    loadingResource: Int,
    private val cellOffsetSource: (() -> Int)? = null,
    private val onReachEnd: () -> Unit
): GenericPagingAdapter<T>(viewResource, loadingResource) {
    private lateinit var cellObserver: RecyclerViewCellVisibilityObserver

    fun setRecyclerview(view: RecyclerView) {
        cellObserver = RecyclerViewCellVisibilityObserver(view)
    }

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