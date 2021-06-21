package dk.mustache.corelib.toolbar_expanding

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.ToolbarDropdownConstraintLayoutBinding
import dk.mustache.corelib.utils.toPx

class ToolbarDropdownConstraintLayout : ConstraintLayout {
    val binding: ToolbarDropdownConstraintLayoutBinding =
        ToolbarDropdownConstraintLayoutBinding.inflate(LayoutInflater.from(context), this, true)
    var overlay: View? = null
        private set(value) {
            if (value!=null) {
                binding.overlayDropdownList.addView(value)
            }
            field = value
        }
    var overlayVisible = false
    var topToolbarHeight = 50.toPx()

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }
    private fun init(context: Context, attrs: AttributeSet?) {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ToolbarDropdownConstraintLayout)
        val title = typedArray.getString(R.styleable.ToolbarDropdownConstraintLayout_title)
        if (title!=null) {
            setTitle(title)
        }

        val subtitle = typedArray.getString(R.styleable.ToolbarDropdownConstraintLayout_subtitle)
        if (subtitle!=null) {
            setSubtitle(subtitle)
        }
        binding.topbarCenterLayout.setOnClickListener {
            val overlayView = overlay
            if (overlayView!=null) {
                if (overlayVisible) {
                    hideOverlay()
                } else {
                    showOverlayView(overlayView)
                }
            }

        }
        typedArray.recycle()

        binding.overlayDropdownContainer.visibility = View.VISIBLE
        this.clipChildren = false
        this.clipToPadding = false
        binding.topbarBackground.clipChildren = true
        binding.topbarBackground.clipToPadding = true
        binding.topbarBackground.clipToOutline = true
        binding.overlayDropdownContainer.clipChildren = false
        binding.overlayDropdownContainer.clipToPadding = false
        binding.overlayDropdownContainer.clipToOutline = false

    }

    fun setTopbarHeight(topbarHeight: Int) {
        this.setPadding(paddingLeft,paddingTop + topbarHeight.toPx(), paddingRight, paddingBottom)
        binding.topbarBackground.translationY = -topbarHeight.toPx().toFloat()
        topToolbarHeight = topbarHeight

        overlay.let {
            viewTreeObserver.addOnGlobalLayoutListener (object:
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.overlayDropdownContainer.translationY =
                        -(topbarHeight + (overlay?.height ?: 0)).toPx().toFloat()
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }

            })
        }

        val params = binding.topbarBackground.layoutParams
        params.height = topToolbarHeight.toPx()
    }

    fun setTitle(title: String?) {
        if (!title.isNullOrEmpty())
            binding.title.text = title
    }

    fun setSubtitle(subtitle: String?) {
        binding.subtitle.visibility = if (!subtitle.isNullOrEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }

        binding.subtitle.text = subtitle
    }

    fun setDropdownOverlay(view: View?) {
        if (overlay==null) {
            overlay = view
        }
    }

    private fun orderLayouts() {
        this.bringChildToFront(binding.overlayDropdownList)
        this.bringChildToFront(binding.topbarBackground)
    }

    fun showOverlayView(view: View, delayMillis: Int = 0) {
        binding.dropdownArrow.animate().rotation(180f).setDuration(
            300
        ).start()
        overlayVisible = true
        setDropdownOverlay(view)
        orderLayouts()
        overlay?.elevation = 21f
        binding.overlayDropdownContainer.animate().setStartDelay(delayMillis.toLong()).translationY(-(topToolbarHeight.toPx().toFloat())).setInterpolator(
            AccelerateDecelerateInterpolator()).setDuration(300).start()
    }

    fun hideOverlay(delayMillis: Int = 0) {
        overlayVisible = false
        binding.overlayDropdownContainer.animate().setStartDelay(delayMillis.toLong()).translationY(-((topToolbarHeight+(overlay?.height?:0))).toPx().toFloat()).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(300).start()
        binding.dropdownArrow.animate().rotation(0f).setDuration(
            300
        ).start()
    }
}