package dk.mustache.corelib.sticky_header_decoration

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.SectionHeaderItemBinding
import dk.mustache.corelib.section_header_list.SectionHeaderListAdapter
import dk.mustache.corelib.section_header_list.SectionItem

open class StickyHeaderListAdapter <T : SectionItem, U: ViewModel> (itemListWithHeaders: ArrayList<SectionItem>, rowItemType: Int, placeholderItemType: Int, headerItemType: Int = R.layout.section_header_item, onItemClicked: ((item: SectionItem) -> Unit)? = {}) : SectionHeaderListAdapter<T, U>(itemListWithHeaders, rowItemType, placeholderItemType, headerItemType, onItemClicked), StickyHeaderItemDecoration.StickyHeaderInterface{

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        var itemPos = itemPosition
        var headerPosition = 0
        do {
            if (this.isHeader(itemPos)) {
                headerPosition = itemPos
                break
            }
            itemPos -= 1
        } while (itemPos >= 0)
        return headerPosition
    }

    override fun getHeaderLayout(headerPosition: Int): Int {
        return headerItemType
    }

    override fun bindHeaderData(header: ViewDataBinding, headerPosition: Int) {
        if(itemCount>0 && headerPosition>-1) {
            bindHeaderView(header, headerPosition)
        }
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return if(itemPosition != -1)
            getItemViewType(itemPosition) == headerItemType
        else false
    }

    //Call this to bind to custom layout
    open fun bindHeaderView(headerBinding: ViewDataBinding, position: Int) {
        headerBinding.executePendingBindings()
    }
}