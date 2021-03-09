package dk.mustache.corelib.bottomsheet_inputdialog

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.Observable
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dk.mustache.corelib.MustacheCoreLib
import dk.mustache.corelib.R

open class BaseBottomSheetFragmentDialog : BottomSheetDialogFragment(), BaseFragment{

    lateinit var inputDialogViewModel: BottomSheetInputViewModel

    fun registerDialogViewModel(viewModel: BottomSheetInputViewModel) {
        viewModel.actionObservable.addOnPropertyChangedCallback(callBack)
    }

    val callBack = object : androidx.databinding.Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            val actionObservable = inputDialogViewModel.actionObservable.get()
            when (actionObservable) {
                ActionPressedEnum.BACK -> {
                    dismissDialog()
                }
                else -> {

                }
            }
            if (actionObservable != ActionPressedEnum.NO_ACTION) {
                inputDialogViewModel.actionObservable.set(ActionPressedEnum.NO_ACTION)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        activity?.window?.statusBarColor = ContextCompat.getColor(MustacheCoreLib.getContextCheckInit(), R.color.white)
        inputDialogViewModel = ViewModelProviders.of(requireActivity()).get(BottomSheetInputViewModel::class.java)
        registerDialogViewModel(inputDialogViewModel)
        super.onResume()
    }

    override fun onPause() {
        inputDialogViewModel.actionObservable.removeOnPropertyChangedCallback(callBack)
        super.onPause()
    }

    open fun dismissDialog(delay: Long = 400) {}
}