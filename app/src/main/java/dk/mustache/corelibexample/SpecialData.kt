package dk.mustache.corelibexample

import dk.mustache.corelib.list_header_viewpager.GenericPagerFragment
import dk.mustache.corelib.list_header_viewpager.PageData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SpecialData <T: GenericPagerFragment> (val specialHeader: String, val clazzz: Class<T>, val topListHeader: String) : PageData<T>(clazzz, topListHeader)