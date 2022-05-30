package dk.mustache.corelib.validation.views

import android.app.DatePickerDialog
import android.content.Context
import android.util.AttributeSet
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import dk.mustache.corelib.utils.MDate
import dk.mustache.corelib.utils.MDateFormat

class ValidationDatePicker : ValidationTextView {
    private val onDateChangedListeners: MutableList<(MDate?) -> Unit> = mutableListOf()
    private val onValidationChangedListeners: MutableList<(Boolean) -> Unit> = mutableListOf()

    private var viewModel: ValidationDateViewModel? = null
    private lateinit var viewId: String
    fun setViewId(value: String) {
        viewId = value
    }

    private var datePickerDialog: DatePickerDialog? = null

    override fun getViewIsValid(): Boolean? {
        return viewModel?.isValid
    }

    fun setIsValid(newValue: Boolean?) {
        viewModel?.apply { isValid = newValue }
    }

    fun getDate(): MDate? {
        return viewModel?.date
    }

    fun setDate(newValue: MDate?) {
        viewModel?.apply { date = newValue }
        val validateResult = viewModel?.validate()
        if (validateResult != null && validateResult.triggerCallbacks) {
            triggerOnValidationChangedListener(validateResult.value)
        }
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
    override fun init(context: Context, attrs: AttributeSet?) {
        super.init(context, attrs)

        setOnClickListener {
            datePickerDialog?.show()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewModel = ViewModelProvider(context as ViewModelStoreOwner).get(
            viewId,
            ValidationDateViewModel::class.java
        )
        viewModel?.onDateChangedListener = ::onValueChanged

        val currentDate = MDate.BuilderDk().now()
        val onDateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val mDate = MDate.BuilderDk().year(year).month(month + 1).day(dayOfMonth).build()
                viewModel?.date = mDate
                val validateResult = viewModel?.validate()
                if (validateResult != null && validateResult.triggerCallbacks) {
                    triggerOnValidationChangedListener(validateResult.value)
                }
            }

        datePickerDialog = DatePickerDialog(
            context,
            onDateSetListener,
            currentDate.year,
            currentDate.month - 1,
            currentDate.day
        )
    }

    private fun onValueChanged(value: MDate?) {
        // Update value of the datepicker base on viewModel date
        if (
            value != null &&
            (datePickerDialog?.datePicker?.year != value.year ||
            datePickerDialog?.datePicker?.month != value.month.minus(1) ||
            datePickerDialog?.datePicker?.dayOfMonth != value.day)
        ) {
            datePickerDialog?.updateDate(
                value.year,
                value.month - 1,
                value.day
            )
        }

        // Update value of textView based on viewModel date
        if (value == null) {
            text = null
            return
        } else if (value.show(MDateFormat.PRETTYDATE_YEAR_SHORT) != text.toString()) {
            text = value.show(MDateFormat.PRETTYDATE_YEAR_SHORT)
        }

        // Inform those listening to the view that the value of the view has been updated
        onDateChangedListeners.forEach { it.invoke(value) }
    }

    fun addOnDateChangedListener(listener: (MDate?) -> Unit) {
        onDateChangedListeners.add(listener)
    }

    private fun triggerOnValidationChangedListener(newValue: Boolean) {
        onValidationChangedListeners.forEach { it.invoke(newValue) }
    }

    override fun addOnValidationChangedListener(listener: (Boolean) -> Unit) {
        onValidationChangedListeners.add(listener)
    }

    fun setValidationType(validationType: Int?) {
        if (validationType != null && viewModel != null) {
            requireNotNull(viewModel).setValidationType(validationType)
        }
    }

    @Suppress("unused")
    companion object {
        @InverseBindingAdapter(attribute = "date")
        @JvmStatic
        fun getDate(view: ValidationDatePicker): MDate? {
            return view.getDate()
        }

        @BindingAdapter("date")
        @JvmStatic
        fun setDate(view: ValidationDatePicker, newValue: MDate?) {
            if (newValue != view.getDate()) {
                view.setDate(newValue)
            }
        }

        @BindingAdapter("app:dateAttrChanged")
        @JvmStatic
        fun setDateListeners(view: ValidationDatePicker, attrChange: InverseBindingListener) {
            view.addOnDateChangedListener { attrChange.onChange() }
        }

        @InverseBindingAdapter(attribute = "isValid")
        @JvmStatic
        fun getViewIsValid(view: ValidationDatePicker): Boolean? {
            return view.getViewIsValid()
        }

        @BindingAdapter("isValid")
        @JvmStatic
        fun setIsValid(view: ValidationDatePicker, newValue: Boolean?) {
            if (newValue != view.getViewIsValid()) {
                view.setIsValid(newValue)
            }
        }

        @BindingAdapter("app:isValidAttrChanged")
        @JvmStatic
        fun setIsValidListeners(view: ValidationDatePicker, attrChange: InverseBindingListener) {
            view.addOnValidationChangedListener { attrChange.onChange() }
        }
    }
}