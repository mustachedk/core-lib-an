package dk.mustache.corelib.validation.validators

import androidx.core.text.isDigitsOnly
import dk.mustache.corelib.R

class DkPhoneValidator: Validator<String> {
    override fun validate(value: String?): ValidationResult {
        if(value.isNullOrEmpty()) {
            return ValidationResult(false, R.string.validation_required_field)
        }
        if(value.isDigitsOnly().not()) {
            return ValidationResult(false, R.string.validation_require_valid_phone_dk_numbers)
        }
        if(value.length != 8) {
            return ValidationResult(false, R.string.validation_require_valid_phone_dk_size)
        }
        return ValidationResult(true)
    }
}