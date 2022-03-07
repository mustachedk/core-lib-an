package dk.mustache.corelib.swipe_accept_layout

import android.content.Context
import android.util.AttributeSet
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.SwipeAcceptLayoutBinding
import dk.mustache.corelib.utils.toPx


class SwipeAcceptLayout : ConstraintLayout, View.OnTouchListener {

    lateinit var binding: SwipeAcceptLayoutBinding
    var targetValue: String = ""
    private var animationInProgress: Boolean = false
    private var swipeStarted: Boolean = false
    private var swipeAccepted: Boolean = false
    private var startX = 0f
    private var startBoundary = 0f
    private var endBoundary = 0f
    private var acceptBoundary = 0f
    var swipeDisabled = false
        set(value) {
            binding.swipeButton.alpha = if (value) {
                0.4f
            } else {
                1f
            }
            field = value
        }
    var swipeListener: SwipeListener? = null

    interface SwipeListener {
        fun onSwipeAccept()
        fun onSwipeStarted()
    }

    fun resetSwipeLayout() {
        swipeAccepted = false
        swipeStarted = false
        animationInProgress = false
        binding.swipeButton.x = this.x - (binding.swipeButton.width/2)
    }

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

    override fun onTouch(view: View, e: MotionEvent): Boolean {
        if (!swipeDisabled) {
            val x = e.x - (binding.swipeButton.width / 2)
            if (x > startBoundary && x < endBoundary && !animationInProgress && swipeStarted)
                binding.swipeButton.x = x

            when (e.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (x < startX + binding.swipeButton.width) {
                        swipeListener?.onSwipeStarted()
                        swipeStarted = true
                        if (x > startBoundary)
                            binding.swipeButton.x = x
                    } else {
                        swipeStarted = false
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (!swipeAccepted) {
                        animationInProgress = true
                        swipeStarted = false
                        if (binding.swipeButton.x > acceptBoundary) {
                            swipeListener?.onSwipeAccept()
                            swipeAccepted = true
                            binding.swipeButton.animate().x(endBoundary - 5.toPx()).setDuration(400)
                                .withEndAction { animationInProgress = false }.start()

                        } else {
                            binding.swipeButton.animate().x(startX).setDuration(400)
                                .withEndAction { animationInProgress = false }.start()
                        }
                    }
                }
                else -> {

                }
            }
        }
        return true
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.swipe_accept_layout,
            this as ViewGroup?,
            true
        )

        setOnTouchListener(this)

        startX = binding.swipeButton.x+5.toPx()
        startBoundary = startX-10.toPx()
        binding.swipeBackground.viewTreeObserver.addOnGlobalLayoutListener {
            endBoundary = binding.swipeBackground.width-75.toPx().toFloat()
            acceptBoundary = (binding.swipeBackground.width-(binding.swipeButton.width+20.toPx())).toFloat()
        }

        val a = context.obtainStyledAttributes(attrs, R.styleable.SwipeAcceptLayout, 0, 0)

        swipeDisabled = a.getBoolean(R.styleable.SwipeAcceptLayout_swipeDisabled, false)

        a.recycle()
    }

    fun setLabel(labelText : String?) {
        if (!labelText.isNullOrBlank()) {
            binding.swipeLabel.visibility = View.VISIBLE
            binding.swipeLabel.text = labelText
        } else {
            binding.swipeLabel.visibility = View.GONE
            binding.swipeLabel.text = ""
        }
    }

}
