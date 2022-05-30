package dk.mustache.corelib.validation.views

import androidx.lifecycle.ViewModel
import dk.mustache.corelib.utils.MDate
import dk.mustache.corelib.validation.validators.AlwaysDateValidator
import dk.mustache.corelib.validation.validators.Min18Validator
import dk.mustache.corelib.validation.validators.ValidationType
import dk.mustache.corelib.validation.validators.Validator


class ValidationDateViewModel: ViewModel() {
    private lateinit var validator: Validator<MDate>

    lateinit var onDateChangedListener: (MDate?) -> Unit
    var date: MDate? = null
    set(value) {
        field = value
        onDateChangedListener(date)

    }

    var isValid: Boolean? = null

    fun setValidationType(validationType: Int) {
        validator = when(validationType) {
            ValidationType.Always -> AlwaysDateValidator()
            ValidationType.Min18 -> Min18Validator()
            else -> throw NotImplementedError("No validationType of the given index is implemented")
        }
    }

    fun validate(): ValidationResult {
        val newIsValid = validator.validate(date)
        val reply = ValidationResult(newIsValid, isValid != newIsValid)
        isValid = newIsValid
        return reply
    }

    data class ValidationResult(val value: Boolean, val triggerCallbacks: Boolean)
}