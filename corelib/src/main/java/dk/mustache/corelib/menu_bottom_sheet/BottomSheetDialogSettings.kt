package dk.mustache.corelib.menu_bottom_sheet

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class BottomSheetDialogSettings(
    val title: String,
    val itemTextList: List<String>,
    val type: MenuDialogType
): Serializable, Parcelable