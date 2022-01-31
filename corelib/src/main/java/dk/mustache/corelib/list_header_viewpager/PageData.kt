package dk.mustache.corelib.list_header_viewpager

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.random.Random

@Parcelize
open class PageData <T: GenericPagerFragment> (val clazz: Class<T>, var topListItemText: String = "", val pageDataId: String = "${Random.nextLong(10000000L)}") : Parcelable