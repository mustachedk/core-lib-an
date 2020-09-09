package dk.mustache.corelib.list_header_viewpager

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller


class CenterLayoutManager : LinearLayoutManager {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State, position: Int) {
        val smoothScroller: SmoothScroller = CenterSmoothScroller(recyclerView.context)
        if (position>=0) {
            smoothScroller.targetPosition = position
            startSmoothScroll(smoothScroller)
        }
    }

    private class CenterSmoothScroller internal constructor(context: Context?) : LinearSmoothScroller(context) {
        override fun calculateDtToFit(viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int {
            return boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
            return 150f / displayMetrics.densityDpi
        }
    }
}