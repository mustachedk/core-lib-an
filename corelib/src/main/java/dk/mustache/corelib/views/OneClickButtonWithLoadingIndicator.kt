package dk.mustache.corelib.views

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import dk.mustache.corelib.MustacheCoreLib
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.LayoutOneclickLoadingIndicatorBinding
import dk.mustache.corelib.utils.ViewUtil
import java.lang.Exception

class OneClickButtonWithLoadingIndicator : ConstraintLayout {

    lateinit var binding : LayoutOneclickLoadingIndicatorBinding
    private var buttonEnabledWhenLoading: Boolean = false

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

    fun init (context: Context, attrs: AttributeSet?) {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_oneclick_loading_indicator,
            this as ViewGroup?,
            true
        )

        val a = context.obtainStyledAttributes(attrs, R.styleable.OneClickButtonWithLoadingIndicator, 0, 0)

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val oneClickFont =
                a.getFont(
                    R.styleable.OneClickButtonWithLoadingIndicator_android_fontFamily
                )
                binding.oneClickButton2.typeface = oneClickFont
            } else {
                //TODO set font on old versions of android
            }
        } catch (e: Exception) {

        }

        val textColor = a.getColor(R.styleable.OneClickButtonWithLoadingIndicator_android_textColor,
        ContextCompat.getColor(MustacheCoreLib.getContextCheckInit(), R.color.red))

        binding.oneClickButton2.setTextColor(textColor)

        val loadingIndicatorColor = a.getColor(R.styleable.OneClickButtonWithLoadingIndicator_loadingIndicatorColor,
            ContextCompat.getColor(MustacheCoreLib.getContextCheckInit(), R.color.red))

        binding.loadingIndicator.setIndeterminateTintList(ColorStateList.valueOf(loadingIndicatorColor));

        val text = a.getString(
            R.styleable.OneClickButtonWithLoadingIndicator_android_text
        )

        val showLoadingIndicator = a.getBoolean(
            R.styleable.OneClickButtonWithLoadingIndicator_showLoadingIndicator, false
        )

        buttonEnabledWhenLoading = a.getBoolean(
            R.styleable.OneClickButtonWithLoadingIndicator_loadingIndicatorEnabledWhenLoading, false
        )

        binding.oneClickButton2.text = text

        binding.oneClickButton2.gravity = TextView.TEXT_ALIGNMENT_CENTER

        setShowLoadingIndicator(showLoadingIndicator)

        a.recycle()
    }

    fun setShowLoadingIndicator(showLoadingIndicator: Boolean) {
        if (showLoadingIndicator) {
            this.isClickable = false
            binding.loadingIndicator.visibility = View.VISIBLE
            binding.oneClickButton2.visibility = View.INVISIBLE
        } else {
            this.isClickable = true
            binding.loadingIndicator.visibility = View.INVISIBLE
            binding.oneClickButton2.visibility = View.VISIBLE
        }
    }

    fun showLoadingIndicator(show: Boolean) {
        setShowLoadingIndicator(show)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        if (l != null) {
            super.setOnClickListener(ViewUtil.getOneClickListener(RESET_DURATION, l))
        } else {
            super.setOnClickListener(l)
        }
    }

    private val RESET_DURATION = 500L
}