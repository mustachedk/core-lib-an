package dk.mustache.corelib.utils

import dk.mustache.corelib.utils.MDateFormat.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * There are four primary types of formats: DATE, PRETTYDATE, TIME and ISOWEEK.
 *
 * Dates and PrettyDates consist of day and month. Add _YEAR to also get year.
 *
 * Time consists of hour and minute. Add _SECONDS to also get seconds.
 */
enum class MDateFormat(val pattern: String) {
    /** Show date according to pattern "yyyy-MM-dd". Example: "2020-08-23". */
    DATE_YEAR_REVERSE("yyyy-MM-dd"),

    /** Show date according to pattern "dd/MM yyyy". Example: "23/08 2020". */
    DATE_YEAR("dd/MM yyyy"),

    /** Show date according to pattern "d/M yyyy". Example: "23/8 2020". */
    DATE_YEAR_SHORT("d/M yyyy"),

    /** Show date according to pattern "dd/MM". Example: "23/08". */
    DATE("dd/MM"),

    /** Show date according to pattern "d/M". Example: "23/8". */
    DATE_SHORT("d/M"),

    /** Show date according to pattern "d. MMMM". Example: "23. august". */
    PRETTYDATE_LONG("d. MMMM"),

    /** Show date according to pattern "d. MMM". Example: "23. aug". */
    PRETTYDATE_SHORT("d. MMM"),

    /** Show date according to pattern "d.MMM". Example: "23.aug". */
    PRETTYDATE_VERYSHORT("d.MMM"),

    /** Show date according to pattern "d. MMMM yyyy". Example: "23. august 2020". */
    PRETTYDATE_YEAR_LONG ("d. MMMM yyyy"),

    /** Show date according to pattern "d. MMM yyyy". Example: "23. aug 2020". */
    PRETTYDATE_YEAR_SHORT ("d. MMM yyyy"),

    /** Show date according to pattern "d.MMM yy". Example: "23.aug 20". */
    PRETTYDATE_YEAR_VERYSHORT ("d.MMM yy"),

    /** Returns the iso week number according to pattern "ww". Example: "07". */
    ISOWEEK_TWODIGIT ("ww"),

    /** Returns the iso week number according to pattern "w". Example: "7". */
    ISOWEEK_NATURAL ("w"),

    /** Show time according to pattern "HH:mm:ss". Example: "13:50:50". */
    TIME_SECONDS ("HH:mm:ss"),

    /** Show time according to pattern "HH:mm". Example: "13:50". */
    TIME ("HH:mm")
}

/**
 * Convenience class allowing the easy manipulation and string
 * formatting of a java Calendar instance.
 *
 * Can add and subtract years, months, days, hours, minutes and seconds
 * (with plusDateTime, plusDate and plusTime as convenience methods.
 *
 * Can output the contained calendar as a formatted string, using the
 * many different "showXXX" methods. Observe the methods' kdoc to see
 * the pattern used.
 *
 * @property calendar The calendar to encapsulate
 * @property locale The locale to use for formatting
 */
class MDate(val calendar: Calendar = Calendar.getInstance(), private val locale: Locale): Comparable<MDate> {

    val year get() = calendar.get(Calendar.YEAR)
    val month get() = calendar.get(Calendar.MONTH)+1
    val day get() = calendar.get(Calendar.DAY_OF_MONTH)
    val hour get() = calendar.get(Calendar.HOUR_OF_DAY)
    val minute get() = calendar.get(Calendar.MINUTE)
    val second get() = calendar.get(Calendar.SECOND)

    /**
     * Show date or time according to provided format. Only PrettyDate
     * formats use the caseType parameter.
     *
     * @param format Format of the MDate as either date or time. See
     *     MDateFormat kdocs for the individual formats.
     * @param caseType Casing/capitalization of month for pretty
     *     formats. Default: LowerCase
     * @return
     */
    fun show(format: MDateFormat, caseType: CaseType = CaseType.LowerCase): String {
        val dateFormat = SimpleDateFormat(format.pattern, locale)
        return when (format) {
            PRETTYDATE_LONG, PRETTYDATE_SHORT, PRETTYDATE_YEAR_LONG, PRETTYDATE_YEAR_SHORT -> dateFormat.format(calendar.time)
                .case(caseType, locale = locale)
            PRETTYDATE_VERYSHORT, PRETTYDATE_YEAR_VERYSHORT -> {
                val dateParts = dateFormat.format(calendar.time).split(".")
                val formattedDateParts = dateParts.mapIndexed { index, part ->
                    if (index == 1) { // Month
                        part.case(caseType, locale = locale)
                    } else { // Day
                        part
                    }
                }
                return formattedDateParts.joinToString(".")
            }
            else -> dateFormat.format(calendar.time)
        }
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

    fun roundToDate(): MDate {
        val newCal = calendar.clone() as Calendar
        newCal.set(Calendar.HOUR_OF_DAY, 0)
        newCal.set(Calendar.MINUTE, 0)
        newCal.set(Calendar.SECOND, 0)
        newCal.set(Calendar.MILLISECOND, 0)
        return MDate(newCal, locale)
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

    override fun compareTo(other: MDate): Int {
        val result = ((this.calendar.timeInMillis - other.calendar.timeInMillis) / 1000).toInt()
        return result
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
 * Use now() without any setters to acquire an MDate instance with the current datetime
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

    fun now(): MDate {
        val currentDateTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance(locale)
        calendar.timeInMillis = currentDateTime
        return MDate(calendar, locale)
    }

    fun build(): MDate {
        val calendar = Calendar.getInstance(locale)
        calendar.set(year, month - 1, day + 1, hour, minute, second)
        return MDate(calendar, locale)
    }
}
