package dk.mustache.corelib.animated_background

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import dk.mustache.corelib.MustacheCoreLib
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.AnimatedBackgroundLayoutBinding


class AnimatedBackgroundLayout : ConstraintLayout {

    lateinit var binding: AnimatedBackgroundLayoutBinding
    var progressTranslationStart = -2000
    var progressTranslationEnd = 2000
    var animationDuration = 2000
    var cornerRadius = 10f
    var mPath: Path? = null
    var valueAnimator: ValueAnimator? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(
            context,
            attrs
        )
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.animated_background_layout,
            this as ViewGroup?,
            true
        )

        val a = context.obtainStyledAttributes(attrs, R.styleable.AnimatedBackgroundLayout, 0, 0)

        val centerColor = a.getColor(
            R.styleable.AnimatedBackgroundLayout_centerGradientColor,
            ContextCompat.getColor(MustacheCoreLib.getContextCheckInit(), R.color.gradient_progress_animation_light)
        )

        val outerColor = a.getColor(
            R.styleable.AnimatedBackgroundLayout_outerGradientColor,
            ContextCompat.getColor(MustacheCoreLib.getContextCheckInit(), R.color.gradient_progress_animation_dark)
        )

        cornerRadius = a.getDimension(
            R.styleable.AnimatedBackgroundLayout_bgCornerRadius,
            10f
        )

        animationDuration = a.getInt(
            R.styleable.AnimatedBackgroundLayout_bgAnimationDuration,
            2000
        )

        setAnimatedBackground(ColorDrawable(outerColor))

        createGradientBackground(outerColor = outerColor, centerColor = centerColor)
        animateBackground(animationDuration)

    }

    fun animateBackground(duration: Int) {
        valueAnimator = ValueAnimator.ofInt(
            progressTranslationStart,
            progressTranslationEnd
        )
        valueAnimator?.addUpdateListener {
            val animationPosition = it.animatedValue as Int
            binding.benefitProgressImage.translationX = animationPosition.toFloat()
        }
        valueAnimator?.duration = duration.toLong()
        valueAnimator?.repeatCount = ValueAnimator.INFINITE
        valueAnimator?.repeatMode = ValueAnimator.RESTART
        valueAnimator?.start()
    }

    fun createGradientBackground(centerColor: Int, outerColor: Int) {

        //Workaround: GradientDrawable does not support center X so we use a LinearGradient and a ShaderFactory instead
        val sf: ShapeDrawable.ShaderFactory = object : ShapeDrawable.ShaderFactory() {
            override fun resize(width: Int, height: Int): Shader {
                return LinearGradient(
                    0f,
                    0f,
                    width.toFloat(),
                    height.toFloat(),
                    intArrayOf(outerColor, centerColor, outerColor),
                    floatArrayOf(0f, 0.5f, 1f),
                    Shader.TileMode.MIRROR
                )
            }
        }

        val p = PaintDrawable()
        p.shape = RectShape()
        p.shaderFactory = sf
        binding.benefitProgressImage.background = p
    }

    fun setAnimationEnabled(enabled: Boolean?) {
        if (enabled == false) {
            valueAnimator?.cancel()
            binding.benefitProgressImage.visibility = View.GONE
        } else {
            binding.benefitProgressImage.visibility = View.VISIBLE
            animateBackground(animationDuration)
        }
    }

    fun setAnimatedBackground(drawable: Drawable) {
        binding.progressBackgroundCl.background = drawable
    }

    override fun dispatchDraw(canvas: Canvas) {
        val path = mPath
        if (path != null) {
            canvas.save()
            canvas.clipPath(path)
            super.dispatchDraw(canvas);
            canvas.restore()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        binding.progressClipView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = RectF(0f, 0f, w.toFloat(), h.toFloat())
            mPath = Path()
            mPath?.addRoundRect(r,
                cornerRadius,
                cornerRadius,
                Path.Direction.CW)
            mPath?.close()
        }
    }
}
