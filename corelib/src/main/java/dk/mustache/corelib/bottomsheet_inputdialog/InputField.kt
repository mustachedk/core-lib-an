package dk.mustache.corelib.bottomsheet_inputdialog

import android.os.Parcelable
import dk.mustache.corelib.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InputField(var id: Int, val label: String, var dataText: String, val hint: String? = null, val inputType: InputFieldTypeEnum, var inputDataType: InputDataTypeEnum, var isSelectionEnabled: Boolean = true, val backgroundResId: Int = R.layout.input_field_edit) :
    Parcelable