package dk.mustache.corelib.price_layout


import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.PssPriceLayoutBinding
import java.util.*

class PriceLayout : ConstraintLayout {

    lateinit var binding: PssPriceLayoutBinding
    var showThousandSeparators = false
    var kr: Int = 0
        set(value) {
            binding.priceText.text = if (showThousandSeparators)
                                        String.format("%,d", value)
                                     else
                                        value.toString()
            field = value
        }
    var ore: Int = 0
        set(value) {
            binding.oreText.text = addZero(value)
            field = value
        }

    var hideLabels: Boolean = false
        set(value) {
            field = value
            if(hideLabels) {
                binding.labelContainer.visibility = View.GONE
            }
        }

    var priceConfigurationItem: PriceConfigurationItem? = null
        set(value) {
            field = value
            if(priceConfigurationItem?.displayYellowOfferPrice() == true) {
                showYellowOfferPrice(priceConfigurationItem)
            }
            if(priceConfigurationItem?.displayDiscountLabel() == true) {
                showDiscountLabel(priceConfigurationItem)
            }
            if(priceConfigurationItem?.displayMaxNumberPerCustomerLabel() == true) {
                showMaxQuantityPerCustomerLabel(priceConfigurationItem)
            }
            if(priceConfigurationItem?.type == 2) {
                showAvoidWasteLabel()
            }
        }

    // Show yellow price with the old price below.
    private fun showYellowOfferPrice(priceConfigurationItem: PriceConfigurationItem?) {
        binding.priceText.setTextColor(ContextCompat.getColor(context, R.color.app_price_background_color))
        binding.oreText.setTextColor(ContextCompat.getColor(context, R.color.app_price_background_color))
        setPrice(priceConfigurationItem?.discountPrice?: -1.0)
        val oldPriceString = String.format(Locale.GERMAN,"%,.2f",priceConfigurationItem?.price)
        val oldPrice = SpannableString(oldPriceString)
        oldPrice.setSpan(StrikethroughSpan(), 0, oldPrice.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.oldPrice.text = oldPrice
        binding.oldPrice.visibility = View.VISIBLE
    }

    // Show yellow label with the discount above the price.
    private fun showDiscountLabel(priceConfigurationItem: PriceConfigurationItem?) {
        binding.discountLabel.text = context.getString(R.string.quantity_discount_label,
            priceConfigurationItem?.discountAmount?.toInt()?.toString(),
            String.format("%.2f", priceConfigurationItem?.discountPrice?: ""))
        binding.discountLabel.visibility = View.VISIBLE
    }

    // Show yellow label with the maximum number pr. customer above the price.
    private fun showMaxQuantityPerCustomerLabel(priceConfigurationItem: PriceConfigurationItem?) {
        binding.maxAmountLabel.text = context.getString(R.string.max_quantity_label,
            priceConfigurationItem?.discountMaxQuantity.toString())
        binding.maxAmountLabel.visibility = View.VISIBLE
        if(priceConfigurationItem?.displayDiscountLabel() == false) {
            binding.discountLabel.visibility = View.GONE
        }
    }

    private fun showAvoidWasteLabel() {
        binding.maxAmountLabel.text = context.getString(R.string.avoid_waste_label)
        binding.maxAmountLabel.visibility = View.VISIBLE
        binding.discountLabel.visibility = View.GONE
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

    private fun init(context: Context?, attrs: AttributeSet?) {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.pss_price_layout,
            this as ViewGroup?,
            true
        )

        val a = context.obtainStyledAttributes(attrs, R.styleable.PSSPriceLayout, 0, 0)

        val oreTopMargin = a.getDimension(
            R.styleable.PSSPriceLayout_oreTextTopPadding,
            4f
        )

        binding.oreText.setPadding(0, oreTopMargin.toInt(), 0, 0)

        val krText = a.getInt(
            R.styleable.PSSPriceLayout_defaultKr,
            0
        )

        binding.priceText.text = krText.toString()

        val oreText = a.getInt(
            R.styleable.PSSPriceLayout_defaultOre,
            0
        )

        binding.oreText.text = oreText.toString()

        val fontId = a.getResourceId(
            R.styleable.PSSPriceLayout_priceTextFont,
            0
        )

        if (fontId > 0) {
            binding.priceText.setTypeface(ResourcesCompat.getFont(getContext(), fontId))
            binding.oreText.setTypeface(ResourcesCompat.getFont(getContext(), fontId))
        } else {
            binding.oreText.setTypeface(ResourcesCompat.getFont(getContext(), R.font.gotham_black))
        }

        val textColor = a.getColor(R.styleable.PSSPriceLayout_priceTextColor, 0)
        if (textColor > 0) {
            binding.priceText.setTextColor(textColor)
            binding.oreText.setTextColor(textColor)
        }

        val krTextSize = a.getInt(
            R.styleable.PSSPriceLayout_krTextSize,
            4
        )

        binding.priceText.textSize = krTextSize.toFloat()

        val oreTextSize = a.getInt(
            R.styleable.PSSPriceLayout_oreTextSize,
            4
        )

        binding.oldPrice.textSize = oreTextSize.toFloat()
        binding.oreText.textSize = oreTextSize.toFloat()

        a.recycle()
    }

    fun addZero(ore: Int): String {
        return if (ore<10) {
            "0${ore}"
        } else {
            "$ore"
        }
    }

    fun setPrice(price: Double) {
        setPrice(price.toString())
    }

    fun setPrice(price: Float) {
        setPrice(price.toString())
    }

    fun setPrice(priceText: String) {
        try {
            val priceSplit = priceText.split(",", ".")
            if (priceSplit.size > 1) {
                kr = priceSplit[0].toInt()
                val oreString = if (priceSplit[1].length==1) {
                    "${priceSplit[1]}0"
                } else {
                    priceSplit[1]
                }
                ore = oreString.toInt()
            } else {
                kr = priceText.toInt()
            }
        } catch (e: Exception) {
            kr = 0
            ore = 0
        }
    }
}