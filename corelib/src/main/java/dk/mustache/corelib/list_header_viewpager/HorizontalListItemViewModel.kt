package dk.mustache.corelib.list_header_viewpager

import androidx.databinding.ObservableField
import dk.mustache.corelib.viewmodels.ObservableBaseViewModel

class HorizontalListItemViewModel (val pageData: PageData<GenericPagerFragment>, val selectionListener: SelectionListener?, val index: Int, val selectedIndex: Int): ObservableBaseViewModel() {

    var selected = ObservableField<Boolean>(index==selectedIndex)

    fun setAsSelectedAndCallback() {
        selected.set(true)
        selectionListener?.headerSelected(pageData, index)
    }
}