package dk.mustache.corelib.list_header_viewpager

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import dk.mustache.corelib.viewmodels.ObservableBaseViewModel

class HorizontalListItemViewModel (val pageData: PageData, val selectionListener: ProductGroupSelectionListener?, val index: Int, val selectedIndex: Int): ObservableBaseViewModel() {

    var selected = ObservableField<Boolean>(index==selectedIndex)
    var paddingEnd = ObservableInt(0)

    fun filterOffersByPageData() {
        selected.set(true)
        selectionListener?.typeSelected(pageData, index)
    }
}