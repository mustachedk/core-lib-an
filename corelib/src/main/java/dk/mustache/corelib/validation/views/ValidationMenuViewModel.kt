package dk.mustache.corelib.validation.views

import dk.mustache.corelib.validation.validators.ValidationResult

class ValidationMenuViewModel : ValidationStringViewModel() {

    var adapter: ValidationMenuAdapter<*>? = null

    fun validateValue(): ValidationResult {
        return validate(adapter?.getSelectedTitle())
    }
}