package dk.mustache.corelib.utils

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener

class RecyclerViewCellVisibilityObserver(private val recyclerView: RecyclerView) {

    fun observeCellVisible(targetPosition: Int, callback: (view: View) -> Unit) {
        val observer = object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val itemCount = recyclerView.adapter?.itemCount ?: 0
                if (itemCount != 0) {
                    val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)
                    val firstPosition = layoutManager.findFirstVisibleItemPosition()
                    val lastPosition = layoutManager.findLastVisibleItemPosition()

                    if(targetPosition in firstPosition..lastPosition) {
                        callback(recyclerView.getChildAt(targetPosition))
                    }
                }
            }
        }
        recyclerView.addOnScrollListener(observer)
    }

    fun observeBottomCellVisible(offset: Int = 0, callback: (position: Int, view: View) -> Unit) {
        val observer = object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val itemCount = recyclerView.adapter?.itemCount ?: 0
                if (itemCount != 0) {
                    val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)

                    val targetPosition = itemCount - offset
                    val lastPosition = layoutManager.findLastVisibleItemPosition()

                    if(lastPosition >= targetPosition) {
                        callback(targetPosition, recyclerView.getChildAt(targetPosition))
                    }
                }
            }
        }
        recyclerView.addOnScrollListener(observer)
    }

    fun clearAllObservers() {
        recyclerView.clearOnScrollListeners()
    }
}