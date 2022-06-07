package dk.mustache.corelib.validation.views

interface ValidationView {
    fun getViewIsValid(): Boolean?
    fun addOnValidationChangedListener(listener: (Boolean) -> Unit)
}