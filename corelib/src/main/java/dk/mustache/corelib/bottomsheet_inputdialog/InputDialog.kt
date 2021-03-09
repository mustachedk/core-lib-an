package dk.mustache.corelib.bottomsheet_inputdialog

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InputDialog(val header: String, val text: String, val buttonText: String, var cancelButtonText: String, var inputFieldList: ArrayList<InputField>? = null, val inputType: InputDialogTypeEnum) :
    Parcelable