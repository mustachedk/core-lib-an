package dk.mustache.corelib.validation.validators

class AlwaysStringValidator: Validator<String> {
    override fun validate(value: String?): Boolean {
        return true
    }
}