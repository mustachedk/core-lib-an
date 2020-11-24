package dk.mustache.corelib.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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

    fun setOnClickListener(l: OnClickListener) {
        onClickListener = l
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.emptystate_layout,
            this as ViewGroup?,
            true
        )

        val a = context.obtainStyledAttributes(attrs, R.styleable.EmptystateLayout, 0, 0)

        val showButton = a.getString(R.styleable.EmptystateLayout_showButton)
        val buttonText = a.getString(R.styleable.EmptystateLayout_buttonText)


        val titleSize = a.getInteger(R.styleable.EmptystateLayout_titleSize, 14)
        val subtitleSize = a.getInteger(R.styleable.EmptystateLayout_subtitleSize, 14)
        val buttonTextSize = a.getInteger(R.styleable.EmptystateLayout_buttonTextSize, 14)

        val titleColor = a.getColor(R.styleable.EmptystateLayout_titleColor, 0)
        val subtitleColor = a.getColor(R.styleable.EmptystateLayout_subtitleColor, 0)
        val buttonTextColor = a.getColor(R.styleable.EmptystateLayout_buttonTextColor, 0)

        binding.emptystateTitle.setTextColor(titleColor)
        binding.emptystateSubtitle.setTextColor(subtitleColor)
        binding.emptystateButton.setTextColor(buttonTextColor)

        binding.emptystateTitle.textSize = titleSize.toFloat()
        binding.emptystateSubtitle.textSize = subtitleSize.toFloat()
        binding.emptystateButton.textSize = buttonTextSize.toFloat()

        if ((showButton?.toLowerCase()?:"")=="true" || (showButton==null && !buttonText.isNullOrBlank())) {
            binding.emptystateButton.visibility = View.VISIBLE
            binding.emptystateButton.text = buttonText
        } else {
            binding.emptystateButton.visibility = View.GONE
        }

        val titleText = a.getString(R.styleable.EmptystateLayout_titleText)
        val subtitleText = a.getString(R.styleable.EmptystateLayout_subtitleText)
        val hideClickEffect = a.getString(R.styleable.EmptystateLayout_hideClickEffect)

        if (hideClickEffect=="true") {
            binding.emptystateButton.foreground = null
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

        if (buttonDrawableId != 0) {
            val drawable = ContextCompat.getDrawable(binding.emptystateButton.context, buttonDrawableId)
            binding.emptystateButton.background = drawable
        }

        val headerFontId: Int = a.getResourceId(R.styleable.EmptystateLayout_titleFont, 0)
        if (headerFontId > 0) {
            binding.emptystateTitle.setTypeface(ResourcesCompat.getFont(getContext(), headerFontId))
        } else {
            binding.emptystateTitle.setTypeface(
                ResourcesCompat.getFont(
                    getContext(),
                    R.font.roboto_bold
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
            binding.emptystateTitle.setTypeface(
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