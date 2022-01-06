package dk.mustache.corelibexample.swipe_recyclerview_item_example

import dk.mustache.corelib.section_header_list.SwipeSectionItem
import dk.mustache.corelib.swipe_recyclerview_item.SwipeDirectionEnum

data class SwipeSectionExampleItem(val header: String, val order: Int, val swiped: Boolean, val id: Long): SwipeSectionItem(headerText2 = header, weight2 = order, isSwiped = swiped, swipeId = id, currentSwipe = SwipeDirectionEnum.NOT_SWIPING)