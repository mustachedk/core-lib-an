package dk.mustache.corelib.animated_progressbar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.AnimatedProgressDrawableLayoutBinding
import dk.mustache.corelib.utils.toPx

class AnimatedProgressForegroundLayout : ConstraintLayout {

    private var currentProgress: Float = 0f
    lateinit var binding: AnimatedProgressDrawableLayoutBinding
    var progressTranslationStart = -1000
    var progressTranslationEnd = 1000
    var cornerRadius = 10
    var isProgressMax = false
    var mPath: Path? = null
    var endStyle: Int = 0
    val triSize = 10f.toPx()
    var valueAnimator : ValueAnimator? = null

    constructor(context: Context) : super(context) { init(context, null) }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init(
        context,
        attrs
    ) }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init(context, attrs) }

    private fun init(context: Context?, attrs: AttributeSet?) {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.animated_progress_drawable_layout,
            this as ViewGroup?,
            true
        )

    }

    fun animateProgressBar() {
        valueAnimator = ValueAnimator.ofInt(
            progressTranslationStart,
            progressTranslationEnd
        )
        valueAnimator?.addUpdateListener {
            val animationPosition = it.animatedValue as Int
            binding.benefitProgressImage.translationX = animationPosition.toFloat()
        }
        valueAnimator?.duration = 2000
        valueAnimator?.repeatCount = ValueAnimator.INFINITE
        valueAnimator?.repeatMode = ValueAnimator.RESTART
        valueAnimator?.start()
    }

    fun setAnimationEnabled(enabled: Boolean?) {
        if (enabled==false) {
            valueAnimator?.cancel()
            binding.benefitProgressImage.visibility = View.GONE
        } else {
            binding.benefitProgressImage.visibility = View.VISIBLE
            animateProgressBar()
        }
    }

    fun setProgressBackground(drawable: Drawable) {
        binding.progressBackgroundColor = drawable
    }

    fun setBackgoundAnimationDrawable(resource: Drawable) {
        binding.animationProgressImageResource = resource
    }

    fun setLabel(text: String) {
        binding.progressLabel.text = text
    }

    fun setEndStyle(endStyle: Int?) {
        this.endStyle = endStyle?:0
    }

    fun setProgress(progress: Float) {
        isProgressMax = progress>=100
        val set = ConstraintSet()
            if (progress<100) {
                set.constrainPercentWidth(R.id.progress_clip_view, (progress / 100f))
            } else {
                set.constrainPercentWidth(R.id.progress_clip_view, 0.9999f)
            }
        currentProgress = progress
        set.applyTo(binding.progressBackgroundCl)
        invalidate()
    }

    override fun dispatchDraw(canvas: Canvas){
        val path = mPath
        if (path!=null) {
            canvas.save()
            canvas.clipPath(path)
            super.dispatchDraw(canvas);
            canvas.restore()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        binding.progressClipView.viewTreeObserver.addOnGlobalLayoutListener {
            val cvWidth = binding.progressClipView.width.toFloat()
            if (isProgressMax) {
                val r = RectF(0f, 0f, w.toFloat(), h.toFloat())
                mPath = Path()
                mPath?.addRoundRect(r, 0f, 0f, Path.Direction.CW)
                mPath?.close()
            } else {
                when(endStyle) {
                    TRIANGLE_STYLE -> {
                        mPath = Path()
                        mPath?.moveTo(cvWidth - triSize, 0f)
                        mPath?.lineTo(cvWidth, (h.toFloat() / 2f))
                        mPath?.lineTo((cvWidth - triSize), h.toFloat())
                        mPath?.lineTo(0f, h.toFloat())
                        mPath?.lineTo(0f, 0f)
                        mPath?.lineTo((cvWidth - triSize), 0f)
                        mPath?.close()
                    }
                    ROUNDED_STYLE -> {
                        val r = RectF(0f, 0f, cvWidth.toFloat(), h.toFloat())
                        mPath = Path()

                        mPath?.addRoundRect(r, cornerRadius.toPx().toFloat(), cornerRadius.toPx().toFloat(), Path.Direction.CW)
                        mPath?.close()
                    }
                    STRAIGHT_STYLE -> {
                        val r = RectF(0f, 0f, cvWidth.toFloat(), h.toFloat())
                        mPath = Path()

                        mPath?.addRect(r, Path.Direction.CW)
                        mPath?.close()
                    }
                }


            }
        }
    }

    companion object {
        const val STRAIGHT_STYLE = 2
        const val ROUNDED_STYLE = 1
        const val TRIANGLE_STYLE = 0
    }
}
