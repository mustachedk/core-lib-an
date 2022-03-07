package dk.mustache.corelib.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginEnd
import androidx.databinding.DataBindingUtil
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.EmptystateLayoutBinding


class EmptyStateView : ConstraintLayout {

    lateinit var binding: EmptystateLayoutBinding
    var clickListener: OnEmptystateActionListener? = null

    interface OnEmptystateActionListener {
        fun onEmptystateClicked()
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

    private var onClickListener: OnClickListener? = null

    override fun setOnClickListener(l: OnClickListener?) {
        onClickListener = l
    }

    fun setButtonOnClickListeners(ll: OnClickListener, rl: OnClickListener? = null) {
        binding.emptystateButton.setOnClickListener(ll)
        binding.emptystateButton2.setOnClickListener(rl)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val a = context.obtainStyledAttributes(attrs, R.styleable.EmptystateLayout, 0, 0)

        val showButton = a.getString(R.styleable.EmptystateLayout_showButton)
        val showButton2 = a.getString(R.styleable.EmptystateLayout_showButton2)
        val buttonText = a.getString(R.styleable.EmptystateLayout_buttonText)
        val button2Text = a.getString(R.styleable.EmptystateLayout_button2Text)


        var titleSize = a.getInteger(R.styleable.EmptystateLayout_titleSize, 0).toFloat()
        var subtitleSize = a.getInteger(R.styleable.EmptystateLayout_subtitleSize, 0).toFloat()
        var buttonTextSize = a.getInteger(R.styleable.EmptystateLayout_buttonTextSize, 0).toFloat()
        if (titleSize==0f) {
            titleSize = a.getDimension(R.styleable.EmptystateLayout_titleSizeSp, 14f)
            subtitleSize = a.getDimension(R.styleable.EmptystateLayout_subtitleSizeSp, 14f)
            buttonTextSize = a.getDimension(R.styleable.EmptystateLayout_buttonTextSizeSp, 14f)
        }
        val buttonLayoutHeight = a.getDimension(R.styleable.EmptystateLayout_buttonLayoutHeight, 0f)

        val expandButtonLayout = a.getBoolean(R.styleable.EmptystateLayout_expandButtonLayoutToEdge, true)

        val titleColor = a.getColor(R.styleable.EmptystateLayout_titleColor, 0)
        val subtitleColor = a.getColor(R.styleable.EmptystateLayout_subtitleColor, 0)
        val buttonTextColor = a.getColor(R.styleable.EmptystateLayout_buttonTextColor, 0)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.emptystate_layout,
            this as ViewGroup?,
            true
        )

        if (expandButtonLayout) {
            val params = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            binding.buttonLayout.layoutParams = params
        } else {

        }

        binding.emptystateTitle.setTextColor(titleColor)
        binding.emptystateSubtitle.setTextColor(subtitleColor)
        binding.emptystateButton.setTextColor(buttonTextColor)

        binding.emptystateTitle.textSize = titleSize
        binding.emptystateSubtitle.textSize = subtitleSize
        binding.emptystateButton.textSize = buttonTextSize

        if ((showButton?.toLowerCase()?:"")=="true" || (showButton==null && !buttonText.isNullOrBlank())) {
            binding.emptystateButton.visibility = View.VISIBLE
            binding.emptystateButton.text = buttonText
        } else {
            binding.emptystateButton.visibility = View.GONE
        }

        if ((showButton2?.toLowerCase()?:"")=="true" || (showButton2==null && !button2Text.isNullOrBlank())) {
            binding.emptystateButton2.visibility = View.VISIBLE
            binding.emptystateButton2.text = button2Text
        } else {
            binding.emptystateButton2.visibility = View.GONE
            val buttonParams = binding.emptystateButton.layoutParams as LinearLayout.LayoutParams
            buttonParams.marginEnd = 0
        }

        if (buttonLayoutHeight>0f) {
            val buttonParams = binding.emptystateButton.layoutParams as LinearLayout.LayoutParams
            buttonParams.height = buttonLayoutHeight.toInt()
            val button2Params = binding.emptystateButton2.layoutParams as LinearLayout.LayoutParams
            button2Params.height = buttonLayoutHeight.toInt()
        }

        val titleText = a.getString(R.styleable.EmptystateLayout_titleText)
        val subtitleText = a.getString(R.styleable.EmptystateLayout_subtitleText)
        val hideClickEffect = a.getString(R.styleable.EmptystateLayout_hideClickEffect)

        if (hideClickEffect=="true") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.emptystateButton.foreground = null
                binding.emptystateButton2.foreground = null
            }
        }

        if (!titleText.isNullOrBlank()) {
            binding.emptystateTitle.text = titleText
        }

        if (!subtitleText.isNullOrBlank()) {
            binding.emptystateSubtitle.text = subtitleText
        }

        val drawableId = a.getResourceId(R.styleable.EmptystateLayout_imageDrawable, 0)

        if (drawableId != 0) {
            val drawable = ContextCompat.getDrawable(binding.emptystateImage.context, drawableId)
            binding.emptystateImage.setImageDrawable(drawable)
        }

        val buttonDrawableId = a.getResourceId(R.styleable.EmptystateLayout_buttonBackground, 0)
        val button2DrawableId = a.getResourceId(R.styleable.EmptystateLayout_button2Background, 0)

        if (buttonDrawableId != 0) {
            val drawable = ContextCompat.getDrawable(binding.emptystateButton.context, buttonDrawableId)
            binding.emptystateButton.background = drawable
        }

        if (button2DrawableId != 0) {
            val drawable = ContextCompat.getDrawable(binding.emptystateButton2.context, button2DrawableId)
            binding.emptystateButton2.background = drawable
        }

        val headerFontId: Int = a.getResourceId(R.styleable.EmptystateLayout_titleFont, 0)
        if (headerFontId > 0) {
            binding.emptystateTitle.setTypeface(ResourcesCompat.getFont(getContext(), headerFontId))
        } else {
            binding.emptystateTitle.setTypeface(
                ResourcesCompat.getFont(
                    getContext(),
                    R.font.gotham_black
                )
            )
        }

        val textFontId: Int = a.getResourceId(R.styleable.EmptystateLayout_subtitleFont, 0)
        if (textFontId > 0) {
            binding.emptystateSubtitle.setTypeface(
                ResourcesCompat.getFont(
                    getContext(),
                    textFontId
                )
            )
        } else {
            binding.emptystateSubtitle.setTypeface(
                ResourcesCompat.getFont(
                    getContext(),
                    R.font.roboto_medium
                )
            )
        }
        setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
            }

        })

        binding.genEmptystateLayout.setOnClickListener {
            clickListener?.onEmptystateClicked()
        }

        a.recycle()

    }

    fun showButton(show: Boolean = true) {
        binding.emptystateButton.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setTitle(title: String?) {
        binding.emptystateTitle.text = title
        showTextLayout(binding.emptystateTitle, title)
    }

    private fun showTextLayout(view: View, text: String?) {
        if (!text.isNullOrBlank()) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    fun setSubtitle(subtitle: String?) {
        binding.emptystateSubtitle.text = subtitle
        showTextLayout(binding.emptystateSubtitle, subtitle)
    }

    private fun showItem(view: View, show: Boolean) {
        when {
            show -> view.visibility = View.VISIBLE
            else -> view.visibility = View.GONE
        }
    }
}
