package dk.mustache.corelib.list_header_viewpager

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.os.Parcelable
import androidx.databinding.ObservableField
import dk.mustache.corelib.R
import dk.mustache.corelib.utils.toPx
import kotlinx.android.parcel.Parcelize

@Parcelize
class HeaderListViewPagerSettings(
    val paddingBetween: Int = 10.toPx(),
    val type: HeaderListViewPagerTypeEnum = HeaderListViewPagerTypeEnum.STRETCH,
    val topListLayoutId: Int = R.layout.top_list_item,
    val topListBackgroundColor: Int = R.color.transparent,
    val offscreenPageLimit: Int = 1,
    val lastItemPaddingEnd: Int = 100,
    private val topListAnchorY: Int = 0,
    private val topListTranslationYStart: Int = 0,
    private val topListTranslationYCurrent: ObservableField<Int> = ObservableField(
        topListTranslationYStart)
) : Parcelable {
    var lockPosition: Boolean = false

    fun setTopListTranslationY(scrollY: Int) {
        if (!lockPosition) {
            if (scrollY > maxScrollRange()) {
                topListTranslationYCurrent.set(topListAnchorY)
            } else {
                topListTranslationYCurrent.set(topListTranslationYStart.plus(-scrollY))
            }
        }
    }

    private fun maxScrollRange() = topListTranslationYStart.minus(topListAnchorY)

    fun getTopListTranslationY() = topListTranslationYCurrent

    fun animateTranslationY(to: Int, duration: Long, interpolator: TimeInterpolator, fillAfter:Boolean) {
        val valueAnimator: ValueAnimator = ValueAnimator.ofInt(topListTranslationYCurrent.get()?:0, to)
        valueAnimator.duration = duration
        valueAnimator.interpolator = interpolator
        valueAnimator.addListener(object :
            Animator.AnimatorListener {

            override fun onAnimationStart(p0: Animator?) {
                lockPosition = true
            }

            override fun onAnimationEnd(p0: Animator?) {
                lockPosition = fillAfter
            }

            override fun onAnimationCancel(p0: Animator?) {
                lockPosition = fillAfter
            }

            override fun onAnimationRepeat(p0: Animator?) {
            }
        })
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Int
            topListTranslationYCurrent.set(value)
        }
        valueAnimator.start()
    }
}
