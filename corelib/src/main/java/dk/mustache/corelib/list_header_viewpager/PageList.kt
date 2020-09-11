package dk.mustache.corelib.list_header_viewpager

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PageList <T : PageData> (var pageList: List<T>) : Parcelable