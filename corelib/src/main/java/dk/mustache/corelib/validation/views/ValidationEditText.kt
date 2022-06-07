package dk.mustache.corelib.validation.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import dk.mustache.corelib.R
import dk.mustache.corelib.utils.StyleUiHelper
import dk.mustache.corelib.validation.validators.ValidationType

class ValidationEditText : AppCompatEditText, ValidationView {
    private val onValidationChangedListeners: MutableList<(Boolean) -> Unit> = mutableListOf()

    private var viewModel: ValidationStringViewModel? = null
    private lateinit var viewId: String
    fun setViewId(value: String) {
        viewId = value
    }

    override fun getViewIsValid(): Boolean? {
        return viewModel?.isValid
    }

    fun setIsValid(newValue: Boolean?) {
        viewModel?.apply { isValid = newValue }
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @Suppress("UNUSED_PARAMETER")
    fun init(context: Context, attrs: AttributeSet?) {
        if(id != NO_ID) {
            viewId = id.toString()
        }

        background = StyleUiHelper.getXmlBackgroundValue(
            context,
            attrs,
            default = R.drawable.validation_edittext_standard_background
        )

        // Update observable if text is updated
        addTextChangedListener({ _, _, _, _ -> }, { _, _, _, _ -> }, { newText ->
            if (getViewIsValid() == false) {
                validate()
            }
        })

        setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validate()
            }
        }

        addOnValidationChangedListener {
            // Update ViewState when isValid changes
            refreshDrawableState()
            postInvalidate()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewModel = ViewModelProvider(context as ViewModelStoreOwner).get(
            viewId,
            ValidationStringViewModel::class.java
        )
    }

    private fun validate() {
        val result = requireNotNull(viewModel).validate(text.toString())
        if (result.triggerCallbacks) {
            triggerOnValidationChangedListener(result.value)
        }
    }

    private fun triggerOnValidationChangedListener(newValue: Boolean) {
        onValidationChangedListeners.forEach { it.invoke(newValue) }
    }

    override fun addOnValidationChangedListener(listener: (Boolean) -> Unit) {
        onValidationChangedListeners.add(listener)
    }

    // Adds state_invalid to drawableState if isValid is false, allowing drawables and other
    // state-aware decorations to read the state
    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val state = if (getViewIsValid() == false) {
            val drawableState = super.onCreateDrawableState(extraSpace + 1)
            val invalidState = intArrayOf(R.attr.state_invalid)
            mergeDrawableStates(drawableState, invalidState)
            drawableState
        } else {
            super.onCreateDrawableState(extraSpace)
        }
        return state
    }

    companion object {
        @BindingAdapter("validationType")
        @JvmStatic
        fun setValidationType(view: ValidationEditText, newValue: Int?) {
            if (newValue != null && view.viewModel != null) {
                requireNotNull(view.viewModel).setValidationType(newValue)
            }
        }

        @InverseBindingAdapter(attribute = "isValid")
        @JvmStatic
        fun getIsValid(view: ValidationEditText): Boolean? {
            return view.getViewIsValid()
        }

        @BindingAdapter("isValid")
        @JvmStatic
        fun setIsValid(view: ValidationEditText, newValue: Boolean?) {
            view.setIsValid(newValue)
        }

        @BindingAdapter("app:isValidAttrChanged")
        @JvmStatic
        fun setListeners(view: ValidationEditText, attrChange: InverseBindingListener) {
            view.addOnValidationChangedListener { attrChange.onChange() }
        }
    }
}