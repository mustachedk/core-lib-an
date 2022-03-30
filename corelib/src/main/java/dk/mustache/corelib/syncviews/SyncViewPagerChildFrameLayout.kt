package dk.mustache.corelib.syncviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import dk.mustache.corelib.databinding.LayoutSyncViewsBinding
import kotlin.random.Random

class SyncViewPagerChildFrameLayout  : EventSyncParentView, EventSyncView {

    override var viewModel: SyncViewsViewModel<*>? = null
    lateinit var binding: LayoutSyncViewsBinding
    private var currentVisibleItemIndex = 0
    private var isAutoScrolling = false
    private var smoothScroll = true
    lateinit var viewPager: ViewPager2

    constructor(context: Context) : super(context) { init(context, null) }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init(context, attrs) }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init(context, attrs) }

    private fun init(context: Context, attrs: AttributeSet?) {

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val viewPagerList = findViewsOfType<ViewPager2>(this)

        if (viewPagerList.isNotEmpty()) {
            viewPager = viewPagerList[0]
            sendPositionOnChange(viewPager)
        }
    }


    fun sendPositionOnChange(viewPager2: ViewPager2) {

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                (viewModel as PositionSyncViewsViewModel).sendSyncEvent(SyncViewEvent(position))
            }
        })
    }

    override var syncListener: SyncViewsViewModel.SyncEventListener<*> = object : SyncViewsViewModel.SyncEventListener<SyncViewEvent> {
        override var id: Int
            get() = 0
            set(value) {}

        override fun receiveEvent(event: SyncViewEvent?) {
            viewPager.setCurrentItem(event?.data?:0, smoothScroll)
        }

    }

}