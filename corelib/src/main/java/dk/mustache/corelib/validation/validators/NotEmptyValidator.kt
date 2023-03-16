package dk.mustache.corelib.validation.validators

import dk.mustache.corelib.R
import dk.mustache.corelib.validation.views.MenuItem

class NotEmptyValidator<T> : Validator<T> {
    override fun validate(value: T?): ValidationResult {
        val validated = if (value == null) {
            false
        } else {
            when (value) {
                is String -> value.isNotBlank()
                is MenuItem<*> -> value.title.isNotBlank()
                else -> false
            }
        }
        return ValidationResult(validated, R.string.validation_required_field)
    }
}