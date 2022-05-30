package dk.mustache.corelib.validation.validators

import dk.mustache.corelib.utils.MDate

class Min18Validator: Validator<MDate> {
    override fun validate(value: MDate?): Boolean {
        if(value == null) return false

        val eighteenYearBirthDay = value.plusDate(years = 18).roundToDate()
        val now = MDate.BuilderDk().now().roundToDate()

        return eighteenYearBirthDay <= now
    }
}