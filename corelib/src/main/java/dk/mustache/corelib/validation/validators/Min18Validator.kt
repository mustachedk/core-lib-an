package dk.mustache.corelib.validation.validators

import dk.mustache.corelib.R
import dk.mustache.corelib.utils.MDate

class Min18Validator: Validator<MDate> {
    override fun validate(value: MDate?): ValidationResult {
        if(value == null) return ValidationResult(false, R.string.validation_required_field)

        val eighteenYearBirthDay = value.plusDate(years = 18).roundToDate()
        val now = MDate.BuilderDk().now().roundToDate()

        return ValidationResult(eighteenYearBirthDay <= now, R.string.validation_require_min_18)
    }
}