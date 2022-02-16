package dk.mustache.corelib.swipe_recyclerview_item

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

open class LockableLayoutManager(context: Context?) : LinearLayoutManager(context) {

    private var isScrollEnabled = true

    fun setScrollEnabled(flag: Boolean) {
        isScrollEnabled = flag
    }

    override fun canScrollVertically(): Boolean {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically()
    }
}