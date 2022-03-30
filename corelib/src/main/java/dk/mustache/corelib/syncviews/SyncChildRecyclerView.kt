package dk.mustache.corelib.syncviews

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.databinding.LayoutSyncViewsBinding
import kotlin.random.Random

class SyncChildRecyclerView  : RecyclerView, EventSyncView {

    override var viewModel: SyncViewsViewModel<*>? = null
    lateinit var binding: LayoutSyncViewsBinding
    private var currentVisibleItemIndex = 0
    private var isAutoScrolling = false
    private var smoothScroll = true

    constructor(context: Context) : super(context) { init(context, null) }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init(context, attrs) }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init(context, attrs) }

    private fun init(context: Context, attrs: AttributeSet?) {

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setOnScrollListener(this)
    }


    fun setOnScrollListener(recyclerView: RecyclerView) {
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
                    (viewModel as PositionSyncViewsViewModel).sendSyncEvent(SyncViewEvent(currentVisibleItemIndex))
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

    override var syncListener: SyncViewsViewModel.SyncEventListener<*> = object : SyncViewsViewModel.SyncEventListener<SyncViewEvent> {
        override var id: Int
            get() = Random.nextInt((System.currentTimeMillis()/1000).toInt())
            set(value) {}

        override fun receiveEvent(event: SyncViewEvent?) {
            isAutoScrolling = true
            val layoutManager = (layoutManager as LinearLayoutManager)
            val position = event?.data?:0
            if(smoothScroll) {
                val smoothScroller = object : LinearSmoothScroller(context) {
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
        }

    }

}