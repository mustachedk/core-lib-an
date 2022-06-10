package dk.mustache.corelib.validation.failureview

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel

class FailureViewModel(
    @StringRes val message: Int,
    @DrawableRes val imageResource: Int? = null,
    @ColorInt val imageTint: Int? = null
) : ViewModel()