package dk.mustache.corelib.syncviews

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.selectable_list.SelectableAdapter
import kotlin.reflect.KFunction2

class RecyclerViewSyncHandler(
    private val recyclerView: RecyclerView,
    conductor: SyncConductor<() -> Int>,
    selectByScroll: Boolean = true,
    private val smoothScroll: Boolean = true
): PositionSyncSubject {
    override var sendEvent: KFunction2<SyncSubject<() -> Int>, () -> Int, Unit>? = null
    override var eventIsOnGoing: Boolean = false
    private var currentVisibleItemIndex = 0

    private var isAutoScrolling = false

    init {
        conductor.subscribe(this)

        if(selectByScroll) {
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (isAutoScrolling) {
                        return
                    }

                    val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)
                    val visibleItemIndex = if (dy > 0) { // Scrolling Up
                        layoutManager.findLastCompletelyVisibleItemPosition()
                    } else { // Scrolling Down
                        layoutManager.findFirstCompletelyVisibleItemPosition()
                    }

                    if (visibleItemIndex != currentVisibleItemIndex && visibleItemIndex != -1) {
                        currentVisibleItemIndex = visibleItemIndex
                        sendPosition(currentVisibleItemIndex)
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        isAutoScrolling = false
                    }
                }
            })
        }
    }

    fun onItemSelected(position: Int) {
        sendPosition(position)
    }

    override fun syncReceive(position: Int) {
        isAutoScrolling = true
        val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)
        if(smoothScroll) {
            val smoothScroller = object : LinearSmoothScroller(recyclerView.context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }
            }
            smoothScroller.targetPosition = position
            layoutManager.startSmoothScroll(smoothScroller)
        }
        else {
            layoutManager.scrollToPositionWithOffset(position, 0)
        }

        val adapter = recyclerView.adapter
        if(adapter is SelectableAdapter<*>) {
            adapter.select(position)
        }
    }
}