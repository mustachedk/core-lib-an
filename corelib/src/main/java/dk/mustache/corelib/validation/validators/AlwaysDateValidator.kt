package dk.mustache.corelib.validation.validators

import dk.mustache.corelib.utils.MDate

class AlwaysDateValidator: Validator<MDate> {
    override fun validate(value: MDate?): Boolean {
        return true
    }
}