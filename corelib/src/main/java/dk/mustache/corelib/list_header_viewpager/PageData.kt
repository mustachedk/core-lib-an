package dk.mustache.corelib.list_header_viewpager

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import kotlin.random.Random

open class PageData <T: GenericPagerFragment> (val clazz: Class<T>, var topListItemText: String = "", val pageDataId: String = "${Random.nextLong(10000000L)}") : Serializable