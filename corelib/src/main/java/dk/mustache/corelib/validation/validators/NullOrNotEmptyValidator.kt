package dk.mustache.corelib.validation.validators

import dk.mustache.corelib.validation.views.MenuItem

class NullOrNotEmptyValidator<T>: Validator<T> {
    override fun validate(value: T?): Boolean {
        if(value == null) {
            return true
        }
        return when(value) {
            is String -> value.isNotBlank()
            is MenuItem<*> -> value.title.isNotBlank()
            else -> false
        }

    }
}