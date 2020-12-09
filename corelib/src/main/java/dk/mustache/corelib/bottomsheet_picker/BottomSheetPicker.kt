package dk.mustache.corelib.bottomsheet_picker

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dk.mustache.corelib.MustacheCoreLib
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.BottomsheetPickerBinding
import kotlin.collections.ArrayList


class BottomSheetPicker <T: Enum<T>> : BottomSheetDialogFragment(), View.OnClickListener {

    private var mListener: BottomSheetPickerListener<T>? = null
    private var paramType: T? = null
    private var values: List<String>? = null
    private var currentValueIndex: Int? = null
    private var topBackgroundColor: Int? = null
    private lateinit var binding: BottomsheetPickerBinding
    private lateinit var headerText: String
    private lateinit var buttonText: String

    interface BottomSheetPickerListener <T : Enum<T>> {
        fun pickerItemSelected(
            paramType: T?,
            value: String,
            selectedIndex: Int
        )
        fun nothingSelected()
    }

    companion object {
        private const val PARAM = "param"
        private const val VALUES = "values"
        private const val BUTTON_TEXT = "button_text"
        private const val HEADER_TEXT = "header_text"
        private const val CURRENT_VALUE_INDEX = "current_value_index"
        private const val TOP_BACKGROUND_COLOR = "top_background_color"

        fun <T: Enum<T>> newInstance(paramType: T, values: List<String>, currentValueIndex: Int, buttonText: String, headerText: String, topBackgroundColor: Int = R.color.light_gray_background): BottomSheetPicker<T> {
            val fragment = BottomSheetPicker<T>()

            val args = Bundle()
            args.putSerializable(PARAM, paramType)
            args.putString(BUTTON_TEXT, buttonText)
            args.putString(HEADER_TEXT, headerText)
            if (values is ArrayList<*>) {
                args.putStringArrayList(VALUES, values as ArrayList<String>?)
            } else if (!values.isNullOrEmpty()){
                args.putStringArrayList(VALUES, ArrayList(values))
            }
            args.putInt(CURRENT_VALUE_INDEX, currentValueIndex)
            args.putInt(TOP_BACKGROUND_COLOR, topBackgroundColor)

            fragment.arguments = args

            return fragment
        }
    }

    //Hack to ensure the navigation buttons are dark and the navigation background is white
    private fun setWhiteNavigationBar(dialog: Dialog) {
        val window: Window? = dialog.window
        if (window != null) {
            val metrics = DisplayMetrics()
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics)
            val dimDrawable = GradientDrawable()
            // ...customize your dim effect here
            val navigationBarDrawable = GradientDrawable()
            navigationBarDrawable.shape = GradientDrawable.RECTANGLE
            navigationBarDrawable.setColor(Color.WHITE)
            val layers = arrayOf<Drawable>(dimDrawable, navigationBarDrawable)
            val windowBackground = LayerDrawable(layers)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                windowBackground.setLayerInsetTop(1, metrics.heightPixels)
            }
            window.setBackgroundDrawable(windowBackground)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            paramType = arguments?.getSerializable(PARAM) as T
            headerText = arguments?.getString(HEADER_TEXT)?:""
            buttonText = arguments?.getString(BUTTON_TEXT)?:""
            values = arguments?.getStringArrayList(VALUES)
            currentValueIndex = arguments?.getInt(CURRENT_VALUE_INDEX)
            topBackgroundColor = arguments?.getInt(TOP_BACKGROUND_COLOR)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BottomsheetPickerBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false

        val d = this.dialog
        if (d!=null)
            setWhiteNavigationBar(d)

        binding.picker.minValue = 0
        binding.picker.maxValue = values?.size?.minus(1) ?: 0
        binding.picker.displayedValues = values?.toTypedArray()
        binding.picker.wrapSelectorWheel = false
        binding.picker.value = currentValueIndex ?: 0
        if (topBackgroundColor!=null)
            binding.dialogStringPickerRoot.setBackgroundColor(ContextCompat.getColor(MustacheCoreLib.context, topBackgroundColor!!))

        binding.pickerHeader.text = headerText
        binding.pickerOk.text = buttonText
        binding.pickerOk.setOnClickListener(this)
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is BottomSheetPickerListener<*>)
            mListener = parentFragment as BottomSheetPickerListener<T>
        else if (activity is BottomSheetPickerListener<*>) {
            mListener = activity as BottomSheetPickerListener<T>
        } else
            throw RuntimeException(parentFragment?.tag + " must implement BottomSheetPickerListener")
    }

    override fun onDetach() {
        super.onDetach()
        mListener?.nothingSelected()
        mListener = null
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.picker_ok -> {
                mListener?.pickerItemSelected(paramType, binding.picker.displayedValues[binding.picker.value], binding.picker.value)
                dismiss()
            }
        }
    }
}
