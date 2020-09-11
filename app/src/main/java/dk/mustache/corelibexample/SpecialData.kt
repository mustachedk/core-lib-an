package dk.mustache.corelibexample

import dk.mustache.corelib.list_header_viewpager.PageData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SpecialData(val specialHeader: String) : PageData()