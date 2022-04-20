package dk.mustache.corelib.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener

class RecyclerViewCellVisibilityObserver(private val recyclerView: RecyclerView) {

    fun observeCellVisible(targetPosition: Int, cellIsVisible: (position: Int) -> Unit) {
        val observer = object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val itemCount = recyclerView.adapter?.itemCount ?: 0
                if (itemCount != 0) {
                    val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)
                    val firstPosition = layoutManager.findFirstVisibleItemPosition()
                    val lastPosition = layoutManager.findLastVisibleItemPosition()

                    if(targetPosition in firstPosition..lastPosition) {
                        cellIsVisible(targetPosition)
                    }
                }
            }
        }
        recyclerView.addOnScrollListener(observer)
    }

    fun observeLastVisibleCellIsPast(targetPosition: Int, lastCellVisible: (position: Int) -> Unit) {
        val observer = object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val itemCount = recyclerView.adapter?.itemCount ?: 0
                if (itemCount != 0) {
                    val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)
                    val lastPosition = layoutManager.findLastVisibleItemPosition()

                    if(lastPosition >= targetPosition) {
                        lastCellVisible(lastPosition)
                    }
                }
            }
        }
        recyclerView.addOnScrollListener(observer)
    }

    fun observeBottomCellVisible(offset: Int = 0, cellIsVisible: (position: Int) -> Unit) {
        val observer = object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val itemCount = recyclerView.adapter?.itemCount ?: 0
                if (itemCount != 0) {
                    val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)

                    val targetPosition = itemCount - offset
                    val lastPosition = layoutManager.findLastVisibleItemPosition()

                    if(lastPosition >= targetPosition) {
                        cellIsVisible(targetPosition)
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