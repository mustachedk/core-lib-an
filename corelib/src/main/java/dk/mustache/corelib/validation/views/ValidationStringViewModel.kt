package dk.mustache.corelib.validation.views

import androidx.lifecycle.ViewModel
import dk.mustache.corelib.validation.validators.*

open class ValidationStringViewModel : ViewModel() {

    private lateinit var validator: Validator<String>

    // null indicates that the view has not yet been validated. This allows the view to show the
    // standard (valid==true) ui, but other views can identify that the view has not yet been
    // validated and thus identify the form as a whole as invalid
    var isValid: Boolean? = null

    var oldValidationMessage: Int? = null

    fun setValidationType(validationType: Int) {
        validator = when (validationType) {
            ValidationType.Always -> {
                isValid =
                    true // set true immediately so user doesn't need to interact with view before it validates as true
                AlwaysStringValidator()
            }
            ValidationType.NotEmpty -> NotEmptyValidator()
            ValidationType.Phone -> DkPhoneValidator()
            ValidationType.NullOrNotEmpty -> NullOrNotEmptyValidator()
            else -> throw NotImplementedError("No validationType of the given index is implemented")
        }
    }

    fun validate(text: String?): ValidationResult {
        val newResult = validator.validate(text)
        val reply = newResult.setTriggerCallbacks(
            newResult.isValid != isValid ||
                    (!newResult.isValid && newResult.message != oldValidationMessage)
        )
        isValid = newResult.isValid
        return reply
    }
}