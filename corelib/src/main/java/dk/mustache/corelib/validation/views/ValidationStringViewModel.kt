package dk.mustache.corelib.validation.views

import androidx.lifecycle.ViewModel
import dk.mustache.corelib.validation.validators.*

open class ValidationStringViewModel: ViewModel() {

    private lateinit var validator: Validator<String>

    var isValid: Boolean? = null

    fun setValidationType(validationType: Int) {
        validator = when(validationType) {
            ValidationType.Always -> AlwaysStringValidator()
            ValidationType.NotEmpty -> NotEmptyValidator()
            ValidationType.Phone -> DkPhoneValidator()
            ValidationType.Password -> TODO()
            ValidationType.NullOrNotEmpty -> NullOrNotEmptyValidator()
            else -> throw NotImplementedError("No validationType of the given index is implemented")
        }
    }

    fun validate(text: String?): ValidationResult {
        val newIsValid = validator.validate(text)
        val reply = ValidationResult(newIsValid, isValid != newIsValid)
        isValid = newIsValid
        return reply
    }

    data class ValidationResult(val value: Boolean, val triggerCallbacks: Boolean)
}