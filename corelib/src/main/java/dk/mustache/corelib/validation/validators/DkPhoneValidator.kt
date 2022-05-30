package dk.mustache.corelib.validation.validators

import androidx.core.text.isDigitsOnly

class DkPhoneValidator: Validator<String> {
    override fun validate(value: String?): Boolean {
        return value?.isDigitsOnly() ?: false && value?.length == 8
    }
}