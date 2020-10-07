package dk.mustache.corelib.list_header_viewpager

import android.os.Parcelable
import androidx.databinding.ObservableField
import dk.mustache.corelib.R
import dk.mustache.corelib.utils.toPx
import kotlinx.android.parcel.Parcelize

@Parcelize
class HeaderListViewPagerSettings(val paddingBetween: Int = 10.toPx(), val type: HeaderListViewPagerTypeEnum = HeaderListViewPagerTypeEnum.STRETCH, val topListLayoutId: Int = R.layout.top_list_item, private val topListAnchorY: Int = 0, private val topListTranslationStart: Int = 0, private val topListTranslationYCurrent: ObservableField<Int> = ObservableField(topListTranslationStart)): Parcelable {
    fun setTopListTranslationY(scrollY: Int) {
        val anchorY = topListAnchorY
        val maxScrollRange = topListTranslationStart.minus(anchorY)
        if (scrollY > maxScrollRange) {
            topListTranslationYCurrent.set(anchorY)
        } else {
            topListTranslationYCurrent.set(topListTranslationStart.plus(-scrollY))
        }
    }
    fun getTopListTranslationY() = topListTranslationYCurrent
}