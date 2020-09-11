package dk.mustache.corelib.list_header_viewpager

import androidx.databinding.ObservableField
import dk.mustache.corelib.viewmodels.ObservableBaseViewModel

open class HeaderListViewPagerViewModel (var selectedIndex: Int = 0, var onItemClicked: ((offer: PageData) -> Unit) = {}) : ObservableBaseViewModel() {
    val pageDataListObservable = ObservableField<List<PageData>>()
}
