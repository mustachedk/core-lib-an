package dk.mustache.corelib.list_header_viewpager

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.graphics.Interpolator
import android.os.Parcelable
import androidx.databinding.ObservableField
import dk.mustache.corelib.R
import dk.mustache.corelib.utils.toPx
import kotlinx.android.parcel.Parcelize

@Parcelize
class HeaderListViewPagerSettings(val paddingBetween: Int = 10.toPx(), val type: HeaderListViewPagerTypeEnum = HeaderListViewPagerTypeEnum.STRETCH, val topListLayoutId: Int = R.layout.top_list_item, private val topListAnchorY: Int = 0, private val topListTranslationStart: Int = 0, private val topListTranslationYCurrent: ObservableField<Int> = ObservableField(topListTranslationStart)): Parcelable {
    private var lockPosition: Boolean = false

    fun lockYPosition(lock:Boolean) {
        lockPosition = true
    }

    fun setTopListTranslationY(scrollY: Int) {
        if (!lockPosition) {
            val maxScrollRange = topListTranslationStart.minus(topListAnchorY)
            if (scrollY > maxScrollRange) {
                topListTranslationYCurrent.set(topListAnchorY)
            } else {
                topListTranslationYCurrent.set(topListTranslationStart.plus(-scrollY))
            }
        }
    }
    fun getTopListTranslationY() = topListTranslationYCurrent

    fun animateY(from:Int, to:Int, duration:Long, interpolator:TimeInterpolator) {
        //ignores lockPosition
        val valueAnimator:ValueAnimator = ValueAnimator.ofInt(from, to)
        valueAnimator.duration = duration
        valueAnimator.interpolator = interpolator
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Int
            topListTranslationYCurrent.set(value)
        }
        valueAnimator.start()
    }
}