package dk.mustache.corelib.validation.views

import androidx.lifecycle.ViewModel
import dk.mustache.corelib.utils.MDate
import dk.mustache.corelib.validation.validators.*


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
        if(this::validator.isInitialized) {
            val newResult = validator.validate(date)
            val reply = newResult.triggerCallbacksIfResultDiffers(isValid)
            isValid = newResult.isValid
            return reply
        }
        else {
            return ValidationResult(false, null, false)
        }
    }
}