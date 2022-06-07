package dk.mustache.corelib.validation.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import dk.mustache.corelib.R
import dk.mustache.corelib.utils.StyleUiHelper

abstract class ValidationTextView : AppCompatTextView, ValidationView {

    private var isInvalid: Boolean = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @Suppress("UNUSED_PARAMETER")
    open fun init(context: Context, attrs: AttributeSet?) {
        // We need to grab focus for other focusable validationviews to lose focus and validate
        isFocusableInTouchMode = true
        // We need to manually call onclick, otherwise first click merely grabs focus
        setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                callOnClick()
            }
        }

        background = StyleUiHelper.getXmlBackgroundValue(
            context,
            attrs,
            R.drawable.validation_edittext_standard_background
        )

        addOnValidationChangedListener {
            isInvalid = !it

            // Update ViewState when isValid changes
            refreshDrawableState()
            postInvalidate()
        }
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val state = if (isInvalid) {
            val drawableState = super.onCreateDrawableState(extraSpace + 1)
            val invalidState = intArrayOf(R.attr.state_invalid)
            mergeDrawableStates(drawableState, invalidState)
            drawableState
        } else {
            super.onCreateDrawableState(extraSpace)
        }
        return state
    }
}