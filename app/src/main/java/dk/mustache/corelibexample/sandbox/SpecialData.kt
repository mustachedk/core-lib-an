package dk.mustache.corelibexample.sandbox

import dk.mustache.corelib.list_header_viewpager.GenericPagerFragment
import dk.mustache.corelib.list_header_viewpager.PageData
import java.io.Serializable

data class SpecialData <T: GenericPagerFragment> (val specialHeader: String, val clazzz: Class<T>, val topListHeader: String) : PageData<T>(clazzz, topListHeader), Serializable