package dk.mustache.corelib.bottomsheet_inputdialog

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import dk.mustache.corelib.MustacheCoreLib
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.BottomSheetInputDialogBinding
import dk.mustache.corelib.databinding.InputFieldCountBinding
import dk.mustache.corelib.databinding.InputFieldEditBinding
import dk.mustache.corelib.databinding.InputFieldTextBinding
import dk.mustache.corelib.utils.toPx

open class BottomSheetInputDialogFragment : BaseBottomSheetFragmentDialog(), BaseFragment, View.OnClickListener {

    private var mListener: BottomSheetInputFieldListener? = null
    private lateinit var inputDialog: InputDialog
    lateinit var binding: BottomSheetInputDialogBinding
    var dismisOnClickOutside = true
    var fadeOnSaveData = true
    var dataSaved = false

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

    companion object {
        private const val INPUT_INPUT_FIELD_LIST = "input_list"

        fun createInputFields(typeEnum: InputDialogTypeEnum): InputDialog {
            return when (typeEnum) {
                InputDialogTypeEnum.SIGNUP -> {
                    InputDialog("test","test","test","test",
                        arrayListOf(InputField(id = 0, label = "test",dataText = "",hint = "test",inputType = InputFieldTypeEnum.CLICKABLE_FIELD, inputDataType = InputDataTypeEnum.STORES),
                            InputField(id = 0, label = "test",dataText = "",hint = "test",inputType = InputFieldTypeEnum.CLICKABLE_FIELD, inputDataType = InputDataTypeEnum.TYPE),
                            InputField(id = 0, label = "test",dataText = "",hint = "test",inputType = InputFieldTypeEnum.EDIT_FIELD, inputDataType = InputDataTypeEnum.PHONE_NUMBER)),typeEnum)
                }
                InputDialogTypeEnum.CARD_DETAILS, InputDialogTypeEnum.ADD_CARD, InputDialogTypeEnum.ADD_OR_EDIT_CARD -> {
                    InputDialog("","","test","test",
                        arrayListOf(InputField(id = 0, label = "test",dataText = "",hint = "test",inputType = InputFieldTypeEnum.EDIT_FIELD, inputDataType = InputDataTypeEnum.CARD_NUMBER),
                            InputField(id = 0, label = "test",dataText = "",hint = "test",inputType = InputFieldTypeEnum.EDIT_FIELD, inputDataType = InputDataTypeEnum.CARD_NAME)),typeEnum)
                }
                InputDialogTypeEnum.LOGIN -> {
                    InputDialog("test","test","test","test",
                        arrayListOf(InputField(id = 0, label = "test",dataText = "",hint = "test",inputType = InputFieldTypeEnum.EDIT_FIELD, inputDataType = InputDataTypeEnum.EMAIL),
                                InputField(id = 0, label = "test",dataText = "",hint = "test",inputType = InputFieldTypeEnum.EDIT_FIELD, inputDataType = InputDataTypeEnum.PHONE_NUMBER)),typeEnum)
                }
                InputDialogTypeEnum.MODIFY_AMOUNT -> {
                    InputDialog("","","","",
                        arrayListOf(InputField(id = 0, label = "",dataText = "1",hint = "",inputType = InputFieldTypeEnum.MODIFY_FIELD, inputDataType = InputDataTypeEnum.MODIFY_ITEM)),typeEnum)
                }
                else -> {
                    InputDialog("","","test","test",
                        arrayListOf(InputField(id = 0, label = "test",dataText = "",hint = "test",inputType = InputFieldTypeEnum.EDIT_FIELD, inputDataType = InputDataTypeEnum.CARD_NUMBER),
                            InputField(id = 0, label = "test",dataText = "",hint = "test",inputType = InputFieldTypeEnum.EDIT_FIELD, inputDataType = InputDataTypeEnum.CARD_NAME)),typeEnum)
                }
            }
        }

        fun newInstance(typeEnum: InputDialogTypeEnum, inputDialog: InputDialog? = null): BottomSheetInputDialogFragment {
            val fList =  if (inputDialog==null)
                createInputFields(typeEnum)
            else
                inputDialog

            val fragment = BottomSheetInputDialogFragment()

            val args = Bundle()
            args.putParcelable(INPUT_INPUT_FIELD_LIST, fList)
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        activity?.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dataSaved = false
    }

    override fun onResume() {
        super.onResume()
        if (inputDialog.inputType != InputDialogTypeEnum.SIGNUP && inputDialog.inputType != InputDialogTypeEnum.LOGIN) {
            activity?.window?.statusBarColor = ContextCompat.getColor(MustacheCoreLib.getContextCheckInit(), R.color.white)
            activity?.window?.navigationBarColor = ContextCompat.getColor(MustacheCoreLib.getContextCheckInit(), R.color.white)
        } else {
            activity?.window?.statusBarColor = ContextCompat.getColor(MustacheCoreLib.getContextCheckInit(), R.color.transparent)
        }
    }

    override fun dismissDialog(delay: Long) {
        activity?.runOnUiThread {
            binding.bottomGradient.animate().alpha(0f).setDuration(200).start()
            binding.scrollInputDialog.animate().translationY(binding.scrollInputDialog.height.toFloat()).setDuration(delay).withEndAction {
                mListener?.cancelInputDialogClicked(inputDialog.inputType)
                if (isAdded) {
                    dismiss()
                }
            }
            binding.backgroundDim.animate().alpha(0f).setDuration(200).withEndAction {

            }.start()
        }
    }

    fun hideActivityIndicator() {
        binding.saveDataButton.text = inputDialog.buttonText
        binding.saveDataButton.isEnabled = true
        binding.activityIndicator.visibility = View.GONE
    }

    fun showActivityIndicator () {
        binding.saveDataButton.text = ""
        binding.saveDataButton.isEnabled = false
        binding.activityIndicator.visibility = View.VISIBLE
    }


    fun saveAndDismissDialog() {
        showActivityIndicator()
        inputDialogViewModel.actionObservable.removeOnPropertyChangedCallback(callBack)
        val keyboard = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        keyboard?.hideSoftInputFromWindow(binding.root.windowToken, 0)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (fadeOnSaveData) {
            binding.backgroundDim.animate().alpha(0f).setDuration(200).withEndAction {
                mListener?.saveDataClicked(inputDialog.inputFieldList
                    ?: ArrayList(), inputDialog.inputType, onCallbackAfterSaveButtonClick)
            }.start()
        } else {
            mListener?.saveDataClicked(inputDialog.inputFieldList
                ?: ArrayList(), inputDialog.inputType,
                onCallbackAfterSaveButtonClick
            )
        }
        dataSaved = true
    }

    val onCallbackAfterSaveButtonClick = { index: Int, close: Boolean ->
        if (close) {
            dismissDialog()
        } else {
            hideActivityIndicator()
            if (index>=0 && index<inputDialog.inputFieldList?.size?:0) {
                val view = binding.inputBoxList.getChildAt(index)
                val input = inputDialog.inputFieldList?.get(index)
                if (input?.inputType == InputFieldTypeEnum.CLICKABLE_FIELD) {
                    val shake: Animation = AnimationUtils.loadAnimation(MustacheCoreLib.getContextCheckInit(), R.anim.shake_xy)
                    view?.startAnimation(shake)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BottomSheetInputDialogBinding.inflate(inflater, container, false)
        if (arguments!=null) {
            inputDialog = arguments?.getParcelable(INPUT_INPUT_FIELD_LIST)?: InputDialog("","","","",null,InputDialogTypeEnum.CUSTOM)
        }

        Handler().postDelayed({
            if (isAdded) {
                binding.backgroundDim.animate().alpha(1f).setDuration(500).start()
            }
        }, 300)


        binding.backgroundDim.setOnClickListener {
            if (dismisOnClickOutside)
                dismissDialog()
        }

        when (inputDialog.inputType) {
            InputDialogTypeEnum.SIGNUP -> {
                dismisOnClickOutside = false
                fadeOnSaveData = false
            }
            InputDialogTypeEnum.LOGIN -> {
                dismisOnClickOutside = false
                fadeOnSaveData = false
            }
            InputDialogTypeEnum.MODIFY_AMOUNT -> {
                dismisOnClickOutside = false
                fadeOnSaveData = false
                binding.backgroundDim.visibility = View.GONE
                binding.inputDialogBackButton.visibility = View.GONE
                binding.saveDataButton.visibility = View.GONE
                binding.bottomGradient.visibility = View.VISIBLE
            }
            InputDialogTypeEnum.ADD_OR_EDIT_CARD, InputDialogTypeEnum.CARD_DETAILS -> {
                dismisOnClickOutside = true
//                AnalyticsTracking.trackScreen(ScreenTracking.editCard.screenName)
            }
            else -> {
                dismisOnClickOutside = true
            }
        }

        binding.inputDialogHeader.text = inputDialog.header
        binding.inputDialogText.text = inputDialog.text

        if (inputDialog.header.isEmpty()) {
            binding.inputDialogHeader.visibility = View.GONE
            binding.inputDialogText.visibility = View.GONE
            binding.inputDialogBackground.setPadding(10.toPx(), 20.toPx(),10.toPx(),0)
        }

        binding.saveDataButton.text = inputDialog.buttonText
        if (inputDialog.cancelButtonText.isNotEmpty()) {
            binding.inputDialogBackButton.text = inputDialog.cancelButtonText
        } else {
            binding.inputDialogBackButton.visibility = View.GONE
            val params = binding.saveDataButton.layoutParams as ConstraintLayout.LayoutParams
            params.setMargins(0,20.toPx(),0,20.toPx())
        }

        binding.inputDialogBackButton.setOnClickListener {
            dismissDialog()
        }

        val inputFieldList = inputDialog.inputFieldList
        inputFieldList?.forEachIndexed { index, inputField ->

            inputField.id = index

            when (inputField.inputType) {
                InputFieldTypeEnum.EDIT_FIELD -> {
                    val v = InputFieldEditBinding.inflate(inflater, binding.inputBoxList, false)
                    v.labelText = inputField.label

                    v.selectionText = inputField.dataText

                    if (!inputField.isSelectionEnabled) {
                        v.inputEdit.background = ContextCompat.getDrawable(MustacheCoreLib.getContextCheckInit(), R.drawable.edit_value_disabled_background)
                        v.inputEdit.isEnabled = false
                    }
                    v.inputEdit.setHintTextColor(ContextCompat.getColor(MustacheCoreLib.getContextCheckInit(), R.color.trans_gray_text))
                    v.inputEdit.hint = inputField.hint

                    if (inputField.inputDataType==InputDataTypeEnum.PHONE_NUMBER) {
                        v.countryCodeLayout.setOnClickListener {
                            Toast.makeText(MustacheCoreLib.getContextCheckInit(), R.string.phone_countrycode_error, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        v.countryCodeLayout.visibility = View.GONE
                    }

                    val params = v.inputFieldLayout.layoutParams as LinearLayout.LayoutParams

                    if (index != 0 && index != inputFieldList.size - 1)
                        params.setMargins(0, 10.toPx(), 0, 0)

                    if (index == inputFieldList.size - 1)
                        params.setMargins(0, 10.toPx(), 0, 10.toPx())

                    v.inputEdit.setOnKeyListener(object : View.OnKeyListener {
                        override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {

                            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                    if (index == binding.inputBoxList.childCount - 1) {
                                        v.clearFocus()
                                        binding.saveDataButton.performClick()
                                    } else {
                                        binding.inputBoxList.getChildAt(index + 1).requestFocus()
                                        if (inputDialog.inputFieldList?.get(index + 1)?.inputType == InputFieldTypeEnum.EDIT_FIELD) {
                                            Handler().postDelayed({
//                                                    showKeyboard(activity)
                                            }, 200)
                                        }
                                    }
                                    return true
                                }
                                return false

                            }
                            return false
                        }
                    })

                    v.inputEdit.setOnFocusChangeListener { view, hasGainedFocus ->

                    }

                    v.inputEdit.addTextChangedListener(object : android.text.TextWatcher {
                        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                            val text = s.toString()
                            inputFieldList[index].dataText = text
                        }

                        override fun afterTextChanged(s: android.text.Editable) {}
                    })

                    if (index==0) {
                        Handler().postDelayed({
                            showKeyboard(activity, v.inputEdit)
                        },500)
                    }

                    when (inputField.inputDataType) {
                        InputDataTypeEnum.PHONE_NUMBER -> {
                            v.inputEdit.inputType = InputType.TYPE_CLASS_NUMBER
                        }
                        InputDataTypeEnum.CARD_NUMBER -> {
                            v.inputEdit.inputType = InputType.TYPE_CLASS_NUMBER
                        }
                        InputDataTypeEnum.CARD_NAME -> {
                            v.inputEdit.inputType = InputType.TYPE_CLASS_TEXT
                        }
                        else -> {

                        }
                    }

                    binding.inputBoxList.addView(v.inputFieldLayout)
                }
                InputFieldTypeEnum.CLICKABLE_FIELD -> {
                    val v = InputFieldTextBinding.inflate(inflater, binding.inputBoxList, false)
                    v.selectionText = inputField.dataText
                    v.labelText = inputField.label
                    v.inputSelection.hint = inputField.hint
                    v.inputSelection.setHintTextColor(ContextCompat.getColor(MustacheCoreLib.getContextCheckInit(), R.color.trans_gray_text))
                    v.inputSelection.text = inputField.dataText
                    if (!inputField.isSelectionEnabled) {
                        v.inputSelection.background = ContextCompat.getDrawable(MustacheCoreLib.getContextCheckInit(), R.drawable.edit_value_disabled_background)
                        v.inputSelection.isEnabled = false
                        v.inputSelection.isClickable = false
                    }

                    val params = v.inputFieldLayout.layoutParams as LinearLayout.LayoutParams

                    if (index != 0)
                        params.setMargins(0, 10.toPx(), 0, 0)

                    if (index == inputFieldList.size - 1)
                        params.setMargins(0, 0, 0, 10.toPx())

                    v.inputSelection.setOnClickListener {
                        inputDialogViewModel.actionObservable.removeOnPropertyChangedCallback(callBack)
                        hideKeyboard(activity)
                        mListener?.inputItemClicked(inputFieldList[index]) {
                            inputDialogViewModel.actionObservable.addOnPropertyChangedCallback(callBack)
                            if (it.isNotEmpty()) {
                                v.selectionText = it
                            } else {
                                inputField.hint
                            }
                            inputField.dataText = it
                        }
                    }

                    binding.inputBoxList.addView(v.root)
                }
                InputFieldTypeEnum.MODIFY_FIELD -> {
                    val v = InputFieldCountBinding.inflate(inflater, binding.inputBoxList, false)
                    v.selectionText = inputField.dataText

                    val params = v.inputFieldLayout.layoutParams as LinearLayout.LayoutParams

                    if (index != 0)
                        params.setMargins(0, 10.toPx(), 0, 0)

                    if (index == inputFieldList.size - 1)
                        params.setMargins(0, 0, 0, 10.toPx())

                    v.addToShoppinglistImage.setOnClickListener {
                        hideKeyboard(activity)
                        val inputFieldTemp = inputFieldList[index]
                        inputFieldTemp.inputDataType = InputDataTypeEnum.ADD_ITEM
                        mListener?.inputItemClicked(inputFieldTemp) {
                            if (it.isNotEmpty()) {
                                v.selectionText = it
                            } else {
                                inputField.dataText
                            }
                            inputField.dataText = it
                        }
                    }

                    v.removeFromShoppinglistImage.setOnClickListener {
                        hideKeyboard(activity)
                        val inputFieldTemp = inputFieldList[index]
                        inputFieldTemp.inputDataType = InputDataTypeEnum.REMOVE_ITEM
                        mListener?.inputItemClicked(inputFieldTemp) {
                            try {
                                if (it.toInt() != 0) {
                                    if (it.isNotEmpty()) {
                                        v.selectionText = it
                                    } else {
                                        inputField.dataText
                                    }
                                    inputField.dataText = it
                                } else {
                                    dismissDialog()
                                }
                            } catch(e: Exception) {

                            }
                        }
                    }

                    binding.inputBoxList.addView(v.root)
                }
            }
        }

        hideActivityIndicator()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveDataButton.setOnClickListener(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val cardDetailsListener = parentFragment
        if (cardDetailsListener is BottomSheetInputFieldListener)
            mListener = cardDetailsListener
        else if (activity is BottomSheetInputFieldListener){
            mListener = activity as BottomSheetInputFieldListener
        } else
            throw RuntimeException(parentFragment?.tag + " must implement BottomSheetInputFieldListener")
    }

    override fun onDetach() {
        super.onDetach()
        if (!dataSaved)
            mListener?.nothingSaved(inputDialog.inputType)
        mListener = null
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.save_data_button -> {
                saveAndDismissDialog()
            }
        }
    }
}