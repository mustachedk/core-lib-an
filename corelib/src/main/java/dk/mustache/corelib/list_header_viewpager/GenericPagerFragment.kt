package dk.mustache.corelib.list_header_viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import java.lang.Exception

open class GenericPagerFragment() : Fragment() {

    lateinit var pageData: PageData<GenericPagerFragment>

    open fun update() {

    }

    open fun scrollToTop() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pageDataFromArgs = arguments?.getParcelable<PageData<GenericPagerFragment>?>(PAGE_DATA)
        pageData = pageDataFromArgs ?: throw Exception("PageData must be set")
    }

    companion object {
        const val PAGE_DATA = "page_data"
    }
}