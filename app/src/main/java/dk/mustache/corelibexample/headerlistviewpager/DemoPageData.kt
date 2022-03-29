package dk.mustache.corelibexample.headerlistviewpager

import dk.mustache.corelib.list_header_viewpager.GenericPagerFragment
import dk.mustache.corelib.list_header_viewpager.PageData
import java.io.Serializable

data class DemoPageData <T: GenericPagerFragment> (
    val demoHeader: String,
    val demoContent: String,
    val clazzz: Class<T>,
    val topListHeader: String) : PageData<T>(clazzz, topListHeader), Serializable
