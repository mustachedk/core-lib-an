package dk.mustache.corelib.bottomsheet_inputdialog

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InputFieldData(val id: Int, var data: String, val inputType: InputFieldTypeEnum, val inputDataTypeEnum: InputDataTypeEnum) :
    Parcelable