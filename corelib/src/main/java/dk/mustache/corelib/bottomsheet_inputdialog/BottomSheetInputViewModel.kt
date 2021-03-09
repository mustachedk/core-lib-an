package dk.mustache.corelib.bottomsheet_inputdialog

import androidx.databinding.ObservableField
import dk.mustache.corelib.viewmodels.ObservableBaseViewModel

class BottomSheetInputViewModel : ObservableBaseViewModel() {
    val actionObservable = ObservableField(ActionPressedEnum.NOT_INITIALIZED)
}