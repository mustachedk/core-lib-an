package dk.mustache.corelib.validation.views

class ValidationMenuViewModel : ValidationStringViewModel() {

    var adapter: ValidationMenuAdapter<*>? = null

    fun validateValue(): ValidationResult {
        return validate(adapter?.getSelectedTitle())
    }
}