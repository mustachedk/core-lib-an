package dk.mustache.corelib.validation.validators

interface Validator<T> {
    fun validate(value: T?): Boolean
}