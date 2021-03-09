package dk.mustache.corelib.bottomsheet_inputdialog

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.EmptystateLayoutBinding
import dk.mustache.corelib.databinding.InputFieldListLayoutBinding

class InputFieldListLayout : LinearLayout {

    lateinit var binding: InputFieldListLayoutBinding
    var listener: BottomSheetInputFieldListener? = null

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

        if (context is BottomSheetInputFieldListener) {
            listener = context
        }

        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val a = context.obtainStyledAttributes(attrs, R.styleable.InputFieldLayout, 0, 0)

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.input_field_list_layout,
                this as ViewGroup?,
                true
        )

        a.recycle()

    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)

        if (child is InputFieldLayout) {

        }
    }

    interface BottomSheetInputFieldListener {
        fun saveDataClicked(
                inputDataList: ArrayList<InputField>,
                inputType: InputDialogTypeEnum,
                onCallbackAfterSaveButtonClick: (index: Int, close: Boolean) -> Unit
        )

        fun cancelInputDialogClicked(
                inputType: InputDialogTypeEnum
        )

        fun inputItemClicked(
                inputData: InputField,
                onDataChange: (newText: String) -> Unit
        )

        fun nothingSaved(inputType: InputDialogTypeEnum)
    }
}
