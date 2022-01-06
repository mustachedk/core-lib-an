package dk.mustache.corelibexample.swipe_recyclerview_item_example

import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import dk.mustache.corelib.databinding.SectionHeaderItemBinding
import dk.mustache.corelib.section_header_list.SectionHeaderItem
import dk.mustache.corelib.section_header_list.SectionItem
import dk.mustache.corelib.section_header_list.SwipeSectionItem
import dk.mustache.corelib.sticky_header_decoration.StickyHeaderListAdapter
import dk.mustache.corelib.swipe_recyclerview_item.SwipeDirectionEnum
import dk.mustache.corelib.swipe_recyclerview_item.SwipeListItemLayout
import dk.mustache.corelib.swipe_recyclerview_item.SwipePositionEnum
import dk.mustache.corelib.swipe_recyclerview_item.SwipeSettingsEnum

open class SwipeListAdapter <T : SwipeSectionItem, Q : ViewModel> (private val swipeItems: List<T>, val viewModel: Q, val swipeDirectionsEnabled: SwipeSettingsEnum, rowLayoutRes: Int, rowLayoutAlternativeRes: Int, headerLayoutRes: Int,
                                                                   val onShoppingListItemClicked: (swipeItem: SectionItem) -> Unit,
                                                                   val onItemSwiped: (view: SwipeListItemLayout?, direction: SwipeDirectionEnum, listItem: SectionItem) -> Unit,
                                                                   val onSwiping: (position: Int,direction: SwipeDirectionEnum, swipeItem: SectionItem) -> Unit,
                                                                   val doLockScroll: (lockScroll: Boolean) -> Unit) : StickyHeaderListAdapter<T, Q>(
    ArrayList(swipeItems), rowLayoutRes, rowLayoutAlternativeRes, headerLayoutRes, {}) {

    private var swipeTempLocked = false
    private var swipedViewPosition = -1
    private var swipedViewDirection = SwipeDirectionEnum.NOT_SWIPING

    fun resetSwipeAndSetNew(position: Int, swipeDirection: SwipeDirectionEnum) {
        Handler(Looper.getMainLooper()).post {
            itemListWithHeaders.forEach {
                if (it is SwipeSectionExampleItem) {
                    if (!it.isSwiped) {
                        it.currentSwipe = SwipeDirectionEnum.NOT_SWIPING
                    }
                    it.isSwiped = false
                }
            }

            if (swipedViewPosition>=0 && position>=0 && swipedViewPosition<itemListWithHeaders.size) {
                notifyItemChanged(swipedViewPosition)
                val swipeItem = itemListWithHeaders[position]
                if (swipeItem is SwipeSectionItem) {
                    swipeItem.isSwiped = true
                    swipeItem.currentSwipe = swipeDirection
                }

                notifyItemChanged(position)
            }

            swipedViewDirection = swipeDirection
            swipedViewPosition = position
            if (position==-1) {
                notifyDataSetChanged()
            }
        }
    }

    fun setupSwipeLayout(swipeLayout: SwipeListItemLayout, position: Int, swipeItem: SectionItem) {
        swipeLayout.setSwipeSettings(swipeDirectionsEnabled)
        swipeLayout.listener = object : SwipeListItemLayout.SwipeLayoutActionListener {
            override fun onLockScroll(lock: Boolean) {
                doLockScroll(lock)
            }

            override fun onSwiping(
                swipeDistance: Float,
                swipeDirection: SwipeDirectionEnum
            ) {
                onSwiping(
                    position, swipeDirection,
                    swipeItem
                )
            }

            override fun onSwipedLeftToRight() {
                resetSwipeAndSetNew(position, SwipeDirectionEnum.LEFT_TO_RIGHT)
                onItemSwiped(
                    swipeLayout, SwipeDirectionEnum.LEFT_TO_RIGHT,
                    swipeItem
                )
            }

            override fun onSwipedRightToLeft() {
                resetSwipeAndSetNew(position, SwipeDirectionEnum.RIGHT_TO_LEFT)
                onItemSwiped(
                    swipeLayout, SwipeDirectionEnum.RIGHT_TO_LEFT,
                    swipeItem
                )
            }

            override fun onClosed() {
                val swipeItem = itemListWithHeaders[position]
            }
        }
        swipeLayout.setOnClickListener {
            onShoppingListItemClicked(swipeItem)
        }
    }

    fun itemLayoutClosed(swipeItem : SwipeSectionItem, position: Int) {
        if (swipeItem is SwipeSectionItem) {
            swipeItem.isSwiped = false
            swipeItem.currentSwipe = SwipeDirectionEnum.NOT_SWIPING
            swipedViewDirection = SwipeDirectionEnum.NOT_SWIPING
        }
        swipedViewPosition = position
    }

    fun findSwipeLayout(view: View, onSwipeLayoutFound: (swipeLayout: SwipeListItemLayout?) -> Unit) {
        if (view is ViewGroup && view !is SwipeListItemLayout) {
            val children = view.children
            children.forEach { childView ->
                if (childView is SwipeListItemLayout) {
                    onSwipeLayoutFound(childView)
                }
            }
        } else {
            if (view is SwipeListItemLayout) {
                onSwipeLayoutFound(view)
            }
        }
    }

    override fun onBindViewHolder(holder: SectionHeaderViewHolder<Q>, position: Int) {
        val swipeItem = itemListWithHeaders[position]

        val root = holder.binding.root

        findSwipeLayout(root) { swipeLayout ->
            if (swipeLayout!=null) {
                if (swipeItem is SwipeSectionExampleItem) {
                    setupSwipeLayout(swipeLayout, holder.absoluteAdapterPosition, swipeItem)
                    if (swipeItem.isSwiped) {
                        when (swipeItem.currentSwipe) {
                            SwipeDirectionEnum.LEFT_TO_RIGHT -> {
                                if (swipeLayout.swipePosition!=SwipePositionEnum.SWIPED_LEFT_TO_RIGHT) {
                                    swipeLayout.setSwipedLeftToRight(false,false)
                                }
                            }
                            SwipeDirectionEnum.RIGHT_TO_LEFT -> {
                                if (swipeLayout.swipePosition!=SwipePositionEnum.SWIPED_RIGHT_TO_LEFT) {
                                    swipeLayout.setSwipedRightToLeft(false, false)
                                }
                            }
                            SwipeDirectionEnum.NOT_SWIPING -> {
                                if (swipeLayout.swipePosition!=SwipePositionEnum.START_POSITION) {
                                    swipeLayout.close(true)
                                }
                            }
                        }
                    } else {
                                if (swipeItem.currentSwipe==SwipeDirectionEnum.LEFT_TO_RIGHT) {
                                    swipeLayout.setSwipedLeftToRight(false, false)
                                    swipeItem.currentSwipe = SwipeDirectionEnum.NOT_SWIPING
                                } else {
                                    if (swipeItem.currentSwipe==SwipeDirectionEnum.RIGHT_TO_LEFT) {
                                        swipeLayout.setSwipedRightToLeft(false, false)
                                        swipeItem.currentSwipe = SwipeDirectionEnum.NOT_SWIPING
                                    }
                                }
                            swipeLayout.close(true)
                    }
                }
            }
        }

        with(holder) {
            bindViewModel(viewModel)
        }

        super.onBindViewHolder(holder, position)
    }

    fun resetSwipe() {
        resetSwipeAndSetNew(-1, SwipeDirectionEnum.NOT_SWIPING)
    }

    override fun bindHeaderView(headerBinding: ViewDataBinding, position: Int) {

        val listItem = itemListWithHeaders[position]

        when (headerBinding) {
            is SectionHeaderItemBinding -> {
                headerBinding.receipt = listItem
            }
            else -> {

            }
        }

        super.bindHeaderView(headerBinding, position)
    }
}
