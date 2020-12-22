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
import dk.mustache.corelib.databinding.BottomsheetDoublePickerBinding
import dk.mustache.corelib.databinding.BottomsheetPickerBinding
import kotlin.collections.ArrayList


class BottomSheetDoublePicker <T: Enum<T>> : BottomSheetDialogFragment(), View.OnClickListener {

    private var mListener: BottomSheetPickerListener<T>? = null
    private var paramType: T? = null
    private var values: ArrayList<List<String>>? = ArrayList()
    private var currentValueIndex1: Int? = null
    private var currentValueIndex2: Int? = null
    private var topBackgroundColor: Int? = null
    private lateinit var binding: BottomsheetDoublePickerBinding
    private lateinit var headerText: String
    private lateinit var buttonText: String

    interface BottomSheetPickerListener <T : Enum<T>> {
        fun pickerItemSelected(
            paramType: T?,
            values: List<String>
        )
        fun nothingSelected()
    }

    companion object {
        private const val PARAM = "param"
        private const val VALUES1 = "values1"
        private const val VALUES2 = "values2"
        private const val BUTTON_TEXT = "button_text"
        private const val HEADER_TEXT = "header_text"
        private const val CURRENT_VALUE_INDEX1 = "current_value_index1"
        private const val CURRENT_VALUE_INDEX2 = "current_value_index2"
        private const val TOP_BACKGROUND_COLOR = "top_background_color"

        fun <T: Enum<T>> newInstance(paramType: T, values1: List<String>, values2: List<String>, currentValueIndex1: Int, currentValueIndex2: Int, buttonText: String, headerText: String, topBackgroundColor: Int = R.color.light_gray_background): BottomSheetDoublePicker<T> {
            val fragment = BottomSheetDoublePicker<T>()

            val args = Bundle()
            args.putSerializable(PARAM, paramType)
            args.putString(BUTTON_TEXT, buttonText)
            args.putString(HEADER_TEXT, headerText)
            if (values1 is ArrayList<*>) {
                args.putStringArrayList(VALUES1, values1 as ArrayList<String>?)
            } else if (!values1.isNullOrEmpty()){
                args.putStringArrayList(VALUES1, ArrayList(values1))
            }

            if (values2 is ArrayList<*>) {
                args.putStringArrayList(VALUES2, values2 as ArrayList<String>?)
            } else if (!values2.isNullOrEmpty()){
                args.putStringArrayList(VALUES2, ArrayList(values2))
            }

            args.putInt(CURRENT_VALUE_INDEX1, currentValueIndex1)
            args.putInt(CURRENT_VALUE_INDEX2, currentValueIndex2)
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
            val v1 = arguments?.getStringArrayList(VALUES1)
            if (v1!=null) {
                values?.add(v1)
            }

            val v2 = arguments?.getStringArrayList(VALUES2)
            if (v2!=null) {
                values?.add(v2)
            }

            currentValueIndex1 = arguments?.getInt(CURRENT_VALUE_INDEX1)
            currentValueIndex2 = arguments?.getInt(CURRENT_VALUE_INDEX2)
            topBackgroundColor = arguments?.getInt(TOP_BACKGROUND_COLOR)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BottomsheetDoublePickerBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false

        val d = this.dialog
        if (d!=null)
            setWhiteNavigationBar(d)

        binding.doublePicker1.minValue = 0
        binding.doublePicker1.maxValue = values?.get(0)?.size?.minus(1) ?: 0
        binding.doublePicker1.displayedValues = values?.get(0)?.toTypedArray()
        binding.doublePicker1.wrapSelectorWheel = false
        binding.doublePicker1.value = currentValueIndex1 ?: 0

        binding.doublePicker2.minValue = 0
        binding.doublePicker2.maxValue = values?.get(1)?.size?.minus(1) ?: 0
        binding.doublePicker2.displayedValues = values?.get(1)?.toTypedArray()
        binding.doublePicker2.wrapSelectorWheel = false
        binding.doublePicker2.value = currentValueIndex2 ?: 0
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
                val selectedList = listOf(binding.doublePicker1.displayedValues[binding.doublePicker1.value], binding.doublePicker2.displayedValues[binding.doublePicker2.value])
                mListener?.pickerItemSelected(paramType, selectedList)
                dismiss()
            }
        }
    }
}
