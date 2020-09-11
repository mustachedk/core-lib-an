package dk.mustache.corelib.list_header_viewpager

import android.os.Parcelable
import dk.mustache.corelib.R
import dk.mustache.corelib.utils.toPx
import kotlinx.android.parcel.Parcelize

@Parcelize
class HeaderListViewPagerSettings(val paddingBetween: Int = 10.toPx(), val type: HeaderListViewPagerTypeEnum = HeaderListViewPagerTypeEnum.STRETCH, val topListLayoutId: Int = R.layout.top_list_item): Parcelable