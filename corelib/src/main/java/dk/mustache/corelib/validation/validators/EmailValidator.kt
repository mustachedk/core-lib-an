package dk.mustache.corelib.validation.validators

import android.text.TextUtils
import android.util.Patterns
import androidx.core.text.isDigitsOnly
import dk.mustache.corelib.R

class EmailValidator: Validator<String> {
    override fun validate(value: String?): ValidationResult {
        if(value.isNullOrEmpty()) {
            return ValidationResult(false, R.string.validation_required_field)
        }
        if(Patterns.EMAIL_ADDRESS.matcher(value).matches().not()) {
            return ValidationResult(false, R.string.validation_require_valid_email)
        }
        return ValidationResult(true)
    }
}