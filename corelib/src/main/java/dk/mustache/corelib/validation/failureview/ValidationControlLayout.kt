package dk.mustache.corelib.validation.failureview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import dk.mustache.corelib.R
import dk.mustache.corelib.validation.views.ValidationView

class ValidationControlLayout : ConstraintLayout {

    private lateinit var failureViewBinding: ViewDataBinding
    private lateinit var validationView: View
    private val failureView
        get() = failureViewBinding.root

    @DrawableRes private var failureViewImage: Int? = null
    @ColorRes private var failureViewImageTint: Int = android.R.color.holo_red_dark
    private var failureViewSide: ViewSide = ViewSide.BOTTOM

    private var defaultFailureMessage = R.string.validation_required_field
    private var alwaysUseTextDefault = false

    private var failureViewVisibilityWhenValid: Int = View.GONE

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
        val defaultValidationTextViewId = R.layout.validation_view_failed_validation_textview

        val a = context.obtainStyledAttributes(attrs, R.styleable.ValidationControlLayout, 0, 0)
        var failureViewId = a.getResourceId(R.styleable.ValidationControlLayout_failure_view, defaultValidationTextViewId)
        defaultFailureMessage = a.getResourceId(R.styleable.ValidationControlLayout_text_default, defaultFailureMessage)
        alwaysUseTextDefault = a.getBoolean(R.styleable.ValidationControlLayout_always_use_text_default, alwaysUseTextDefault)
        failureViewImage = a.getResourceId(R.styleable.ValidationControlLayout_failure_view_image, -1)
        failureViewImageTint = a.getResourceId(R.styleable.ValidationControlLayout_failure_view_image_tint, failureViewImageTint)
        failureViewSide = ViewSide.fromId(
            a.getInt(
                R.styleable.ValidationControlLayout_failure_view_side,
                ViewSide.BOTTOM.id
            )
        ) ?: ViewSide.BOTTOM
        failureViewVisibilityWhenValid = a.getInt(R.styleable.ValidationControlLayout_failure_view_valid_visibility, failureViewVisibilityWhenValid)
        a.recycle()

        // If use defined an image, and hasn't defined custom failureView, we make sure to use
        // the default validationImageView instead of the validationTextView (which can't host an
        // image)
        if(failureViewImage != -1 && failureViewId == defaultValidationTextViewId)
            failureViewId = R.layout.validation_view_failed_validation_imageview

        inflateFailureView(failureViewId)
        updateViewModelOnFailureView(defaultFailureMessage)
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
        else {if (alwaysUseTextDefault || message == null) {
                updateViewModelOnFailureView(defaultFailureMessage)
            } else {
            updateViewModelOnFailureView(message)
            }
            failureView.visibility = View.VISIBLE
        }
    }

    private fun updateViewModelOnFailureView(text: Int) {
        val viewModel = FailureViewModel(
            message = text,
            imageResource = failureViewImage,
            imageTint = ContextCompat.getColor(context, failureViewImageTint)
        )
        failureViewBinding.setVariable(BR.viewModel, viewModel)
//        if(failureViewImage != -1)
//            (failureView as AppCompatImageView).setColorFilter(failureViewImageTint)
    }

    @SuppressLint("WrongConstant")
    private fun inflateFailureView(@LayoutRes layoutResId: Int) {
        val inflater = LayoutInflater.from(context)

        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, layoutResId, this, true)
        this.failureViewBinding = binding
        failureView.visibility = failureViewVisibilityWhenValid

        attachFailureView()
    }

    private fun attachValidationView() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)
        if(failureViewSide != ViewSide.BOTTOM) {
            constraintSet.connectToParent(validationView, BOTTOM)
        }
        if(failureViewSide != ViewSide.END) {
            constraintSet.connectToParent(validationView, END)
        }
        if(failureViewSide != ViewSide.START) {
            constraintSet.connectToParent(validationView, START)
        }
        if(failureViewSide != ViewSide.TOP) {
            constraintSet.connectToParent(validationView, TOP)
        }

        if (this::failureViewBinding.isInitialized) {
            when(failureViewSide) {
                ViewSide.BOTTOM -> constraintSet.connect(validationView.id, BOTTOM, failureView.id, TOP, 4)
                ViewSide.END -> constraintSet.connect(validationView.id, END, failureView.id, START, 4)
                ViewSide.START -> constraintSet.connect(validationView.id, START, failureView.id, END, 4)
                ViewSide.TOP -> constraintSet.connect(validationView.id, TOP, failureView.id, BOTTOM, 4)
            }
        }
        constraintSet.applyTo(this)
    }

    private fun attachFailureView() {
        val failureView = requireNotNull(this.failureViewBinding).root
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)
        if(failureViewSide != ViewSide.BOTTOM) {
            constraintSet.connectToParent(failureView, TOP)
        }
        if(failureViewSide != ViewSide.END) {
            constraintSet.connectToParent(failureView, START)
        }
        if(failureViewSide != ViewSide.START) {
            constraintSet.connectToParent(failureView, END)
        }
        if(failureViewSide != ViewSide.TOP) {
            constraintSet.connectToParent(failureView, BOTTOM)
        }
        constraintSet.applyTo(this)
        if (this::validationView.isInitialized) {
            when(failureViewSide) {
                ViewSide.BOTTOM -> constraintSet.connect(validationView.id, BOTTOM, failureView.id, TOP, 4)
                ViewSide.END -> constraintSet.connect(validationView.id, END, failureView.id, START, 4)
                ViewSide.START -> constraintSet.connect(validationView.id, START, failureView.id, END, 4)
                ViewSide.TOP -> constraintSet.connect(validationView.id, TOP, failureView.id, BOTTOM, 4)
            }
        }
        constraintSet.applyTo(this)
    }

    private fun ConstraintSet.connectToParent(view: View, side: Int) {
        this.connect(view.id, side, PARENT_ID, side)
    }

    private enum class ViewSide(val id: Int) {
        BOTTOM(0), END(1), START(2), TOP(3);

        companion object {
            fun fromId(id: Int): ViewSide? {
                return values().firstOrNull {side -> side.id == id}
            }
        }
    }
}

