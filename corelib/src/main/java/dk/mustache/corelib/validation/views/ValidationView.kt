package dk.mustache.corelib.validation.views

import androidx.annotation.StringRes

interface ValidationView {
    fun getViewIsValid(): Boolean?
    fun addOnValidationChangedListener(listener: (Boolean, Int?) -> Unit)
}