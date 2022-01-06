package dk.mustache.corelib.section_header_list

import dk.mustache.corelib.swipe_recyclerview_item.SwipeDirectionEnum

open class SwipeSectionItem(var headerText2: String? = "", var weight2: Int = -Int.MAX_VALUE, var isSwiped: Boolean = false, var currentSwipe: SwipeDirectionEnum = SwipeDirectionEnum.NOT_SWIPING, val swipeId: Long = 0L, var isHeader2: Boolean? = false, var isPlaceholder2: Boolean? = false): SectionItem(headerText2, weight2, isHeader2, isPlaceholder2)