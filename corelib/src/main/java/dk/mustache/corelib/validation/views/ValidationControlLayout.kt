package dk.mustache.corelib.validation.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import dk.mustache.corelib.R

class ValidationControlLayout : ConstraintLayout {

    private lateinit var failureViewBinding: ViewDataBinding
    private lateinit var validationView: View
    private val failureView
        get() = failureViewBinding.root

    private var defaultFailureMessage = ""
    private var alwaysUseTextDefault = false

    private var failureViewVisibilityWhenValid: Int = 8

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
        val a = context.obtainStyledAttributes(attrs, R.styleable.ValidationControlLayout, 0, 0)
        val failureViewId = a.getResourceId(R.styleable.ValidationControlLayout_failure_view, R.layout.validation_view_failed_validation_textview)
        defaultFailureMessage = a.getString(R.styleable.ValidationControlLayout_text_default) ?: defaultFailureMessage
        alwaysUseTextDefault = a.getBoolean(R.styleable.ValidationControlLayout_always_use_text_default, alwaysUseTextDefault)
        failureViewVisibilityWhenValid = a.getInt(R.styleable.ValidationControlLayout_failure_view_valid_visibility, 8)
        a.recycle()

        inflateFailureView(failureViewId)
        setFailureTextOnView(defaultFailureMessage)
    }

    override fun onViewAdded(view: View?) {
        super.onViewAdded(view)

        if (view is ValidationView) {
            validationView = view
            view.addOnValidationChangedListener(::onValidationEvent)

            attachValidationView()
        }
    }

    @SuppressLint("WrongConstant")
    private fun onValidationEvent(isValid: Boolean, message: Int?) {
        if(isValid) {
            failureView.visibility = failureViewVisibilityWhenValid
        }
        else {
            if (alwaysUseTextDefault || message == null) {
                setFailureTextOnView(defaultFailureMessage)
            } else {
                setFailureTextOnView(context.getString(message))
            }
            failureView.visibility = View.VISIBLE
        }
    }

    @SuppressLint("WrongConstant")
    private fun inflateFailureView(@LayoutRes layoutResId: Int) {
        val inflater = LayoutInflater.from(context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, layoutResId, this, true)
        this.failureViewBinding = binding
        binding.root.visibility = failureViewVisibilityWhenValid

        attachFailureView()
    }

    private fun attachValidationView() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)
        constraintSet.connectToParent(validationView, TOP)
        constraintSet.connectToParent(validationView, START)
        constraintSet.connectToParent(validationView, END)

        if (this::failureViewBinding.isInitialized) {
            constraintSet.connect(validationView.id, BOTTOM, failureView.id, TOP)
        }
        constraintSet.applyTo(this)
    }

    private fun attachFailureView() {
        val failureView = requireNotNull(this.failureViewBinding).root
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)
        constraintSet.connectToParent(failureView, START)
        constraintSet.connectToParent(failureView, END)
        constraintSet.connectToParent(failureView, BOTTOM)
        constraintSet.applyTo(this)
        if (this::validationView.isInitialized) {
            constraintSet.connect(validationView.id, BOTTOM, failureView.id, TOP)
        }
        constraintSet.applyTo(this)
    }

    private fun setFailureTextOnView(text: String) {
        failureViewBinding.setVariable(BR.validationMessage, text)
    }

    private fun ConstraintSet.connectToParent(view: View, side: Int) {
        this.connect(view.id, side, PARENT_ID, side)
    }
}

