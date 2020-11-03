package dk.mustache.corelib.fragment_dialog

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FragmentDialogSetup (val header: String, val text: String, val dialogType: DialogTypeEnum, val alternativeLayout: Int = 0, val alternativeStyle: Int = 0, val text2: String? = null): Parcelable