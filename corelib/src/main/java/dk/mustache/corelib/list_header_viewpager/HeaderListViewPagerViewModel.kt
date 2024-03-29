package dk.mustache.corelib.list_header_viewpager

import android.app.Activity
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import dk.mustache.corelib.viewmodels.ObservableBaseViewModel

open class HeaderListViewPagerViewModel : ObservableBaseViewModel() {
    var currentShownPage: Int = 0
    var selectedIndexObservable = ObservableInt(-1)
    var pageScrollStateObservable = ObservableInt(0)
    val pageDataListObservable = ObservableField<List<PageData<GenericPagerFragment>>>()
    val settings = ObservableField<HeaderListViewPagerSettings>(HeaderListViewPagerSettings())
    fun <T : PageData<*>> updatePageDataList(pageList: List<T>) {
        //TODO make generic instead of cast to List<PageData<GenericPagerFragment> the issue is that the android
        // ViewModel cannot be instantiated with generic types
        pageDataListObservable.set(pageList as List<PageData<GenericPagerFragment>>)
    }

    fun <T : PageData<U>, U : GenericPagerFragment> getGenericPageList(): List<T>? {
        return pageDataListObservable.get()?.map {
            it as T
        }
    }

    companion object {
        fun getInstance(activity: FragmentActivity): HeaderListViewPagerViewModel {
            return ViewModelProvider(activity).get(HeaderListViewPagerViewModel::class.java)
        }
    }
}
