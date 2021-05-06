package dk.mustache.corelib.animated_progressbar

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import dk.mustache.corelib.MustacheCoreLib
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.AnimatedProgressLayoutBinding
import dk.mustache.corelib.databinding.ProgressDotLayoutBinding
import dk.mustache.corelib.databinding.ProgressEndLayoutBinding
import dk.mustache.corelib.utils.toPx

class AnimatedProgressLayout : ConstraintLayout {
    private var progressInt: Int = 0
    private var forceShowProgressInPercentage: Boolean = false
    private var maxProgress: Int = 0
    private var endLabelPostFix: String = ""
    private var progressLabelPostFix: String = ""
    private var progressAnimationDuration: Int = 0
    private var currentProgress: Float = 0f
    private var animateProgress = false
    lateinit var binding: AnimatedProgressLayoutBinding
    var targetValue: String = ""
    private var showDots: Boolean = false
    var mPath: Path? = null
    var cornerRadius = 0f
    var endLabelColor:Int = 0

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
            R.layout.animated_progress_layout,
            this as ViewGroup?,
            true
        )
        val a = context.obtainStyledAttributes(attrs, R.styleable.AnimatedProgressLayout, 0, 0)

        binding.endStyle = a.getInt(R.styleable.AnimatedProgressLayout_progressBarEndStyle, 0)
        binding.backgroundAnimationEnabled = a.getBoolean(R.styleable.AnimatedProgressLayout_backgroundAnimationEnabled, true)


        val progressBackgroundAnimation = a.getDrawable(R.styleable.AnimatedProgressLayout_backgroundAnimationDrawable)
        if (progressBackgroundAnimation!=null) {
            setProgressBackgroundAnimation(progressBackgroundAnimation)
        } else {
            val drawable = ContextCompat.getDrawable(MustacheCoreLib.getContextCheckInit(), R.drawable.progress_bar_animate6)
            if (drawable!=null)
                setProgressBackgroundAnimation(drawable)
        }

        val backgroundDrawable = a.getDrawable(R.styleable.AnimatedProgressLayout_backgroundDrawable)
        if (backgroundDrawable!=null) {
            setLayoutBackground(backgroundDrawable)
        }

        val progressIndentationFloat = a.getFloat(
            R.styleable.AnimatedProgressLayout_progressIndentationPercentage, 0f
        )
        setProgressIndentationPercentage(progressIndentationFloat)

        progressInt = a.getInt(
            R.styleable.AnimatedProgressLayout_currentProgress,
            0
        )

        maxProgress = a.getInt(
            R.styleable.AnimatedProgressLayout_maxProgress,
            100
        )
        binding.progressDrawableLayout.maxProgress = maxProgress

        endLabelPostFix = a.getString(R.styleable.AnimatedProgressLayout_endLabelPostfix)?:"%"
        progressLabelPostFix = a.getString(R.styleable.AnimatedProgressLayout_progressLabelPostfix)?:"%"
        forceShowProgressInPercentage =
            a.getBoolean(R.styleable.AnimatedProgressLayout_forceShowProgressInPercentage, false)

        val cornerRadius = a.getInt(
            R.styleable.AnimatedProgressLayout_backgroundCornerRadius,
            10
        )

        val progressBackgroundColor = a.getColor(
            R.styleable.AnimatedProgressLayout_progressBackgroundColor,
            ContextCompat.getColor(MustacheCoreLib.getContextCheckInit(), R.color.gradient_progress_animation_dark)
        )

        val progressLabelColor = a.getColor(
            R.styleable.AnimatedProgressLayout_progressLabelColor,
            ContextCompat.getColor(MustacheCoreLib.getContextCheckInit(), R.color.gradient_progress_animation_dark)
        )

        setProgressLabelTextColor(progressLabelColor)

        endLabelColor = a.getColor(
            R.styleable.AnimatedProgressLayout_endLabelColor,
            ContextCompat.getColor(MustacheCoreLib.getContextCheckInit(), R.color.button_gradient_end)
        )

        animateProgress = a.getBoolean(R.styleable.AnimatedProgressLayout_animateProgress, false)
        progressAnimationDuration = a.getInt(R.styleable.AnimatedProgressLayout_progressAnimationDuration, 0)

        showDots = a.getBoolean(R.styleable.AnimatedProgressLayout_showDots, false)

        setProgressBackgroundColor(ColorDrawable(progressBackgroundColor))

        setCornerRadius(cornerRadius)

        setProgress(progressInt.toFloat())

        setEndLabel(endLabelColor)

        a.recycle()
    }

    fun setLayoutBackground(drawable: Drawable) {
        binding.animatedProgressLayout.background = drawable
    }

    fun setProgressIndentationPercentage(indentationPercentage:Float) {
        binding.progressDrawableLayout.progressIndentationPercentage = indentationPercentage
    }

    fun setEndLabelTextColor(colorId: Int) {
        endLabelColor = colorId
        binding.endLabel.setTextColor(colorId)
    }

    fun setProgressLabelTextColor(colorId: Int) {
        binding.progressDrawableLayout.binding.progressLabel.setTextColor(colorId)
    }

    fun setProgressBackgroundColor(drawable: Drawable) {
        binding.progressDrawableLayout.binding.setProgressBackgroundColor(drawable)
    }

    fun setBackgroundAnimationEnabled(enabled: Boolean) {
        binding.progressDrawableLayout.setAnimationEnabled(enabled)
    }

    fun setProgressBackgroundAnimation(drawable: Drawable) {
        binding.progressDrawableLayout.setBackgoundAnimationDrawable(drawable)
    }

    fun setMaxProgress(maxProgress:Int) {
        this.maxProgress = maxProgress
        binding.progressDrawableLayout.maxProgress = maxProgress
        setEndLabel(endLabelColor)
        setProgress(currentProgress)
    }

    private fun setProgressLabel(text: String) {
        binding.progressDrawableLayout.setLabel(text)
    }

    private fun setEndLabel(endLabelColor: Int) {
        if (currentProgress.toInt() == maxProgress) {
            binding.endLabel.visibility = INVISIBLE
        } else {
            binding.endLabel.visibility = VISIBLE
            binding.endLabel.setTextColor(endLabelColor)
            binding.endLabel.text = "$maxProgress$endLabelPostFix"
        }
    }

    fun setEndLabelPost(text: String) {
        endLabelPostFix = text
    }

    fun setTarget(text: String) {
        targetValue = text
    }

    fun setProgress(updatedProgress: Float) {
        if (animateProgress) {
            val valueAnimator = ValueAnimator.ofFloat(currentProgress, updatedProgress)
            valueAnimator.addUpdateListener {
                val value = it.animatedValue as Float
                binding.progressDrawableLayout.setProgress(value)
                binding.progressDrawableLayout.setLabel(progressLabel(value))
            }
            valueAnimator.duration = progressAnimationDuration.toLong()
            valueAnimator.interpolator = DecelerateInterpolator()
            valueAnimator.addListener(object: Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    updateEndLabelIfProgressIsMax(updatedProgress)
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }
            })
            valueAnimator.start()
        } else {
            binding.progressDrawableLayout.setProgress(updatedProgress)
            binding.progressDrawableLayout.setLabel(progressLabel(updatedProgress))
            updateEndLabelIfProgressIsMax(updatedProgress)
        }
        currentProgress = updatedProgress
    }

    private fun updateEndLabelIfProgressIsMax(updatedProgress: Float) {
        if (updatedProgress.toInt() == maxProgress) {
            setEndLabel(endLabelColor)
        }
    }

    private fun progressLabel(updatedProgress: Float) = if (forceShowProgressInPercentage) {
        "${updatedProgress.div(maxProgress).times(100).toInt()}%"
    } else {
        "${updatedProgress.toInt()}$progressLabelPostFix"
    }

    fun setNumberOfProgressDots(numOfDots: Int) {
        binding.endLabel.visibility = View.GONE
        binding.progressDots.removeAllViews()
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        for(i in 0 until numOfDots) {
            if (i<numOfDots-1) {
                val dotLayout = ProgressDotLayoutBinding.inflate(inflater)
                dotLayout.dotImage.visibility = if(!showDots) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
                val dotLayoutRoot = dotLayout.root
                dotLayoutRoot.layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1f
                    )

                binding.progressDots.addView(dotLayoutRoot)
            } else {
                val dotLayout = ProgressEndLayoutBinding.inflate(inflater)
                dotLayout.progressText.text = "$numOfDots"
                val endLayout = dotLayout.root
                endLayout.layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1f
                    )

                binding.progressDots.addView(endLayout)
            }
        }
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
        val r = RectF(0f, 0f, w.toFloat(), h.toFloat())
        mPath = Path()
        mPath?.addRoundRect(r, cornerRadius, cornerRadius, Path.Direction.CW)
        mPath?.close()
    }

    fun setCornerRadius(radius: Int) {
        cornerRadius = radius.toFloat().toPx()
        binding.progressDrawableLayout.cornerRadius = radius
        invalidate()
    }
}
