package dk.mustache.corelib.validation.validators

import android.text.TextUtils
import android.util.Patterns

class EmailValidator: Validator<String> {
    override fun validate(value: String?): Boolean {
        return !TextUtils.isEmpty(value) && Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }
}