package dk.mustache.corelib.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Convenience class allowing the easy manipulation and string formatting of a java Calendar
 * instance.
 *
 * Can add and subtract years, months, days, hours, minutes and seconds (with plusDateTime,
 * plusDate and plusTime as convenience methods.
 *
 * Can output the contained calendar as a formatted string, using the many different
 * "showXXX" methods. Observe the methods' kdoc to see the pattern used.
 *
 * @property calendar The calendar to encapsulate
 * @property locale The locale to use for formatting
 */
class MDate(val calendar: Calendar = Calendar.getInstance(), private val locale: Locale) {

    /** Show date according to pattern "yyyy-MM-dd". */
    fun showYearMonthDay(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", locale)
        return dateFormat.format(calendar.time)
    }

    /** Show date according to pattern "dd/MM yyyy". */
    fun showDayMonthYear(): String {
        val dateFormat = SimpleDateFormat("dd/MM yyyy", locale)
        return dateFormat.format(calendar.time)
    }

    /** Show date according to pattern "d/M yyyy". */
    fun showShortDayMonthYear(): String {
        val dateFormat = SimpleDateFormat("d/M yyyy", locale)
        return dateFormat.format(calendar.time)
    }

    /** Show date according to pattern "dd/MM". */
    fun showDayMonth(): String {
        val dateFormat = SimpleDateFormat("dd/MM", locale)
        return dateFormat.format(calendar.time)
    }

    /** Show date according to pattern "d/M". */
    fun showShortDayMonth(): String {
        val dateFormat = SimpleDateFormat("d/M", locale)
        return dateFormat.format(calendar.time)
    }

    /**
     * Show date according to pattern "d. MMMM". Month is
     * cased/capitalized according to caseType variable.
     *
     * @param caseType Casing/capitalization of month. Default:
     *     LowerCase
     */
    fun showLongPrettyDate(caseType: CaseType = CaseType.LowerCase): String {
        val dateFormat = SimpleDateFormat("d. MMMM", locale)
        return dateFormat.format(calendar.time).case(caseType, locale = locale)
    }

    /**
     * Show date according to pattern "d. MMM". Month is
     * cased/capitalized according to caseType variable.
     *
     * @param caseType Casing/capitalization of month. Default:
     *     LowerCase
     */
    fun showShortPrettyDate(caseType: CaseType = CaseType.LowerCase): String {
        val dateFormat = SimpleDateFormat("d. MMM", locale)
        return dateFormat.format(calendar.time).case(caseType, locale = locale)
    }

    /**
     * Show date according to pattern "d.MMM". Month is
     * cased/capitalized according to caseType variable.
     *
     * @param caseType Casing/capitalization of month. Default:
     *     LowerCase
     */
    fun showVeryShortPrettyDate(caseType: CaseType = CaseType.LowerCase): String {
        val dateFormat = SimpleDateFormat("d.", locale)
        val dateFormat2 = SimpleDateFormat("MMM", locale)
        return dateFormat.format(calendar.time) +
                dateFormat2.format(calendar.time).case(caseType, locale = locale)
    }

    /**
     * Show date according to pattern "d. MMMM yyyy". Month is
     * cased/capitalized according to caseType variable.
     *
     * @param caseType Casing/capitalization of month. Default:
     *     LowerCase
     */
    fun showLongPrettyDateYear(caseType: CaseType = CaseType.LowerCase): String {
        val dateFormat = SimpleDateFormat("d. MMMM yyyy", locale)
        return dateFormat.format(calendar.time).case(caseType, locale = locale)
    }

    /**
     * Show date according to pattern "d. MMM yyyy". Month is
     * cased/capitalized according to caseType variable.
     *
     * @param caseType Casing/capitalization of month. Default:
     *     LowerCase
     */
    fun showShortPrettyDateYear(caseType: CaseType = CaseType.LowerCase): String {
        val dateFormat = SimpleDateFormat("d. MMM yyyy", locale)
        return dateFormat.format(calendar.time).case(caseType, locale = locale)
    }

    /**
     * Show date according to pattern "d.MMM yy". Month is
     * cased/capitalized according to caseType variable.
     *
     * @param caseType Casing/capitalization of month. Default:
     *     LowerCase
     */
    fun showVeryShortPrettyDateYear(caseType: CaseType = CaseType.LowerCase): String {
        val dateFormat = SimpleDateFormat("d.", locale)
        val dateFormat2 = SimpleDateFormat("MMM yy", locale)
        return dateFormat.format(calendar.time) +
                dateFormat2.format(calendar.time).case(caseType, locale = locale)
    }

    /**
     * Returns the iso week number as a string.
     *
     * @param padded Whether to force two digits, zero-padding if
     *     necessary. Default: false.
     * @return
     */
    fun showIsoWeek(padded: Boolean = false): String {
        val weekPattern = if (padded) {
            "ww"
        } else {
            "w"
        }
        val dateFormat = SimpleDateFormat(weekPattern, locale)
        return dateFormat.format(calendar.time)
    }

    /** Show time according to pattern "HH:mm:ss". */
    fun showTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm:ss", locale)
        return dateFormat.format(calendar.time)
    }

    /** Show time according to pattern "HH:mm". */
    fun showHourMinute(): String {
        val dateFormat = SimpleDateFormat("HH:mm", locale)
        return dateFormat.format(calendar.time)
    }

    /**
     * Adds the given values to the year, months, day of month,
     * hours, minute and second of day. Will roll over into correct
     * year/month/day/hour/minute if month/day/hour/minute/second fall
     * outside their normal range.
     *
     * @param years Number of years to add (or subtract for negative
     *     values)
     * @param months Number of months to add (or subtract for negative
     *     values)
     * @param days Number of days to add (or subtract for negative
     *     values)
     * @param hours Number of hours to add (or subtract for negative
     *     values)
     * @param minutes Number of minutes to add (or subtract for negative
     *     values)
     * @param seconds Number of seconds to add (or subtract for negative
     *     values)
     * @return a new MDate instance with the result
     */
    fun plusDateTime(
        years: Int = 0,
        months: Int = 0,
        days: Int = 0,
        hours: Int = 0,
        minutes: Int = 0,
        seconds: Int = 0
    ): MDate {
        val newCal = calendar.clone() as Calendar
        newCal.add(Calendar.YEAR, years)
        newCal.add(Calendar.MONTH, months)
        newCal.add(Calendar.DAY_OF_MONTH, days)
        newCal.add(Calendar.HOUR_OF_DAY, hours)
        newCal.add(Calendar.MINUTE, minutes)
        newCal.add(Calendar.SECOND, seconds)
        return MDate(newCal, locale)
    }

    /**
     * Adds the given values to the year, months and day of month. Will
     * roll over into correct year/month if month/day fall outside their
     * normal range.
     *
     * @param years Number of years to add (or subtract for negative
     *     values)
     * @param months Number of months to add (or subtract for negative
     *     values)
     * @param days Number of days to add (or subtract for negative
     *     values)
     * @return a new MDate instance with the result
     */
    fun plusDate(years: Int = 0, months: Int = 0, days: Int = 0): MDate {
        val newCal = calendar.clone() as Calendar
        newCal.add(Calendar.YEAR, years)
        newCal.add(Calendar.MONTH, months)
        newCal.add(Calendar.DAY_OF_MONTH, days)
        return MDate(newCal, locale)
    }

    /**
     * Adds the given value to the months. Will roll over to correct
     * year if month adds up more than 12 or less than 1.
     *
     * @param months Number of months to add (or subtract for negative
     *     values)
     * @return a new MDate instance with the result
     */
    fun plusMonths(months: Int): MDate {
        return add(Calendar.MONTH, months)
    }

    /**
     * Adds the given value to the day of month. Will roll over into
     * correct month if day of month adds up more than the number of
     * days in the month or less than 1.
     *
     * @param days Number of days to add (or subtract for negative
     *     values)
     * @return a new MDate instance with the result
     */
    fun plusDays(days: Int): MDate {
        return add(Calendar.DAY_OF_MONTH, days)
    }

    /**
     * Adds the given values to the hours, minute and second of day.
     * Will roll over into correct day/hour/minute if hour/minute/second
     * fall outside their normal range.
     *
     * @param hours Number of hours to add (or subtract for negative
     *     values)
     * @param minutes Number of minutes to add (or subtract for negative
     *     values)
     * @param seconds Number of seconds to add (or subtract for negative
     *     values)
     * @return a new MDate instance with the result
     */
    fun plusTime(hours: Int = 0, minutes: Int = 0, seconds: Int = 0): MDate {
        val newCal = calendar.clone() as Calendar
        newCal.add(Calendar.HOUR_OF_DAY, hours)
        newCal.add(Calendar.MINUTE, minutes)
        newCal.add(Calendar.SECOND, seconds)
        return MDate(newCal, locale)
    }

    /**
     * Adds the given value to the hours of day. Will roll over into
     * correct day if hours adds up more than 23 or less than 0.
     *
     * @param hours Number of hours to add (or subtract for negative
     *     values)
     * @return a new MDate instance with the result
     */
    fun plusHours(hours: Int): MDate {
        return add(Calendar.HOUR_OF_DAY, hours)
    }

    /**
     * Adds the given value to the minutes of hour. Will roll over into
     * correct hour if minutes adds up more than 59 or less than 0.
     *
     * @param minutes Number of minutes to add (or subtract for negative
     *     values)
     * @return a new MDate instance with the result
     */
    fun plusMinutes(minutes: Int): MDate {
        return add(Calendar.MINUTE, minutes)
    }

    /**
     * Adds the given value to the seconds of minute. Will roll over
     * into correct minute if seconds adds up more than 59 or less than
     * 0.
     *
     * @param seconds Number of seconds to add (or subtract for negative
     *     values)
     * @return a new MDate instance with the result
     */
    fun plusSeconds(seconds: Int): MDate {
        return add(Calendar.SECOND, seconds)
    }

    private fun add(field: Int, value: Int): MDate {
        val newCal = calendar.clone() as Calendar
        newCal.add(field, value)
        return MDate(newCal, locale)
    }

    companion object {
        /**
         * @return An MDateBuilder using danish localization by default
         */
        fun BuilderDe(): MDateBuilder {
            val locales = Calendar.getAvailableLocales()
            val locale = locales.firstOrNull { it.country == "DE" && it.language == "de" }
                ?: Locale.getDefault()
            return MDateBuilder(locale)
        }

        /**
         * @return An MDateBuilder using danish localization by default
         */
        fun BuilderGb(): MDateBuilder {
            val locales = Calendar.getAvailableLocales()
            val locale = locales.firstOrNull { it.country == "GB" && it.language == "en" }
                ?: Locale.getDefault()
            return MDateBuilder(locale)
        }

        /**
         * @return An MDateBuilder using danish localization by default
         */
        fun BuilderDk(): MDateBuilder {
            val locales = Calendar.getAvailableLocales()
            val locale = locales.firstOrNull { it.country == "DK" && it.language == "da" }
                ?: Locale.getDefault()
            return MDateBuilder(locale)
        }

        /**
         * @return An MDateBuilder using the default localization for
         *     device
         */
        fun Builder(): MDateBuilder {
            return MDateBuilder(Locale.getDefault())
        }
    }
}

/**
 * A builder for the MDate datetime class. Used to initialize year,
 * month, day, hour, minute, second and localization.
 *
 * Setters: dateTime, date, time, year, month, day, hour, minute, second
 *
 * Use build() to acquire an MDate instance matching the given values.
 *
 * @property locale The localization that should be used by the MDate
 *     instance. Defaults to the device's default locale.
 */
class MDateBuilder(private val locale: Locale = Locale.getDefault()) {
    private var year: Int = -1
    private var month: Int = -1
    private var day: Int = -1
    private var hour: Int = -1
    private var minute: Int = -1
    private var second: Int = -1

    fun dateTime(
        year: Int = 2000,
        month: Int = 1,
        day: Int = 1,
        hour: Int = 0,
        minute: Int = 0,
        second: Int = 0
    ): MDateBuilder {
        this.year = year
        this.month = month
        this.day = day
        this.hour = hour
        this.minute = minute
        this.second = second
        return this
    }

    fun date(year: Int = 2000, month: Int = 1, day: Int = 1): MDateBuilder {
        this.year = year
        this.month = month
        this.day = day
        return this
    }

    fun year(year: Int): MDateBuilder {
        this.year = year
        return this
    }

    fun month(month: Int): MDateBuilder {
        this.month = month
        return this
    }

    fun day(day: Int): MDateBuilder {
        this.day = day
        return this
    }

    fun time(hour: Int = 0, minute: Int = 0, second: Int = 0): MDateBuilder {
        this.hour = hour
        this.minute = minute
        this.second = second
        return this
    }

    fun hour(hour: Int): MDateBuilder {
        this.hour = hour
        return this
    }

    fun minute(minute: Int): MDateBuilder {
        this.minute = minute
        return this
    }

    fun second(second: Int): MDateBuilder {
        this.second = second
        return this
    }

    fun build(): MDate {
        val calendar = Calendar.getInstance(locale)
        calendar.set(year, month - 1, day + 1, hour, minute, second)
        return MDate(calendar, locale)
    }
}