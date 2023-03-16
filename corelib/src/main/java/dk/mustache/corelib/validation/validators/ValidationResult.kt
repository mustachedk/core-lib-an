package dk.mustache.corelib.validation.validators

import androidx.annotation.StringRes

data class ValidationResult(
    val isValid: Boolean,
    @StringRes val message: Int? = null,
    val triggerCallbacks: Boolean = true
) {
    fun setTriggerCallbacks(newValue: Boolean): ValidationResult {
        return ValidationResult(isValid, message, newValue)
    }

    fun triggerCallbacksIfResultDiffers(oldResult: Boolean?): ValidationResult {
        return ValidationResult(isValid, message, oldResult != isValid)
    }
}