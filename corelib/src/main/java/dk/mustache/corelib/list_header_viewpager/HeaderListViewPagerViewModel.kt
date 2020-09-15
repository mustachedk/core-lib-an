package dk.mustache.corelib.list_header_viewpager

import androidx.databinding.ObservableField
import dk.mustache.corelib.viewmodels.ObservableBaseViewModel
import java.lang.Exception

open class HeaderListViewPagerViewModel : ObservableBaseViewModel() {
    var selectedIndex: Int = 0
    val pageDataListObservable = ObservableField<List<PageData<GenericPagerFragment>>>()
    val settings = ObservableField<HeaderListViewPagerSettings>(HeaderListViewPagerSettings())

    fun <T : PageData<*>> updatePageDataList(pageList: List<T>) {
        //TODO make generic instead of cast to List<PageData<GenericPagerFragment> the issue is that the android
        // ViewModel cannot be instantiated with generic types
        pageDataListObservable.set(pageList as List<PageData<GenericPagerFragment>>)
    }

    fun <T : PageData<U>,U : GenericPagerFragment> getGenericPageList(): List<T>? {
        return pageDataListObservable.get()?.map {
                it as T
        }
    }
}
