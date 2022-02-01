package dk.mustache.corelib.list_header_viewpager

import android.os.Parcelable
import androidx.databinding.ObservableField
import dk.mustache.corelib.R
import dk.mustache.corelib.utils.toPx
import kotlinx.android.parcel.Parcelize

@Parcelize
class HeaderListViewPagerSettings(
    val paddingBetweenItems: Int = 10.toPx(),
    val type: HeaderListViewPagerTypeEnum = HeaderListViewPagerTypeEnum.STRETCH,
    val filterLayoutId: Int = R.layout.top_list_item,
    val filterBackgroundColor: Int = R.color.transparent,
    val offscreenPageLimit: Int = 1,
    val lastItemPaddingEnd: Int = 100,
    val firstItemPaddingStart: Int = 0,
    val snapCenter: Boolean = true,
    //Swipe sensitivity: The lower the value the more sensitive for horizontal swipe. Should be > 0
    val swipeSensitivity: Int = 6,
    val horizontalListHeight: Int = 0,
    val compatibilityModePreVersion123 : Boolean = true,
    private val filterAnchorY: Int = 0,
    private val filterTranslationYStart: Int = 0,
) : Parcelable {
    private val filterTranslationYCurrent: ObservableField<Int> = ObservableField(filterTranslationYStart)

    fun setTopListTranslationY(scrollY: Int) {
        if (scrollY > maxScrollRange()) {
            filterTranslationYCurrent.set(filterAnchorY)
        } else {
            filterTranslationYCurrent.set(filterTranslationYStart.plus(-scrollY))
        }
    }

    private fun maxScrollRange() = filterTranslationYStart.minus(filterAnchorY)

    fun getTopListTranslationY() = filterTranslationYCurrent
}
