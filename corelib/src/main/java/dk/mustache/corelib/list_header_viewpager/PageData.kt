package dk.mustache.corelib.list_header_viewpager

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class PageData <T: GenericPagerFragment> (val clazz: Class<T>, var topListItemText: String = "") : Parcelable