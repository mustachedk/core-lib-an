package dk.mustache.corelib.utils

import dk.mustache.corelib.utils.MDateFormat.*
import org.joda.time.Days
import org.joda.time.LocalDate
import java.text.SimpleDateFormat
import java.util.*

/**
 * There are four primary types of formats: DATE, PRETTYDATE, TIME and
 * ISOWEEK.
 *
 * Dates and PrettyDates consist of day and month. Add _YEAR to also get
 * year.
 *
 * Time consists of hour and minute. Add _SECONDS to also get seconds.
 */
enum class MDateFormat(val pattern: String) {
    /**
     * Show date according to pattern "yyyy-MM-dd". Example:
     * "2020-08-23".
     */
    DATE_YEAR_REVERSE("yyyy-MM-dd"),

    /**
     * Show date according to pattern "dd/MM yyyy". Example: "23/08
     * 2020".
     */
    DATE_YEAR("dd/MM yyyy"),

    /**
     * Show date according to pattern "d/M yyyy". Example: "23/8 2020".
     */
    DATE_YEAR_SHORT("d/M yyyy"),

    /** Show date according to pattern "dd/MM". Example: "23/08". */
    DATE("dd/MM"),

    /** Show date according to pattern "d/M". Example: "23/8". */
    DATE_SHORT("d/M"),

    /**
     * Show date according to pattern "d. MMMM". Example: "23. august".
     */
    PRETTYDATE_LONG("d. MMMM"),

    /**
     * Show date according to pattern "d. MMM". Example: "23. aug.".
     */
    PRETTYDATE_SHORT("d. MMM"),

    /** Show date according to pattern "d.MMM". Example: "23.aug.". */
    PRETTYDATE_VERYSHORT("d.MMM"),

    /**
     * Show date according to pattern "d. MMMM yyyy". Example: "23.
     * august 2020".
     */
    PRETTYDATE_YEAR_LONG("d. MMMM yyyy"),

    /**
     * Show date according to pattern "d. MMM yyyy". Example: "23. aug.
     * 2020".
     */
    PRETTYDATE_YEAR_SHORT("d. MMM yyyy"),

    /**
     * Show date according to pattern "d.MMM yy". Example: "23.aug. 20".
     */
    PRETTYDATE_YEAR_VERYSHORT("d.MMM yy"),

    /**
     * Show date according to pattern "EEE d. MMMM". Example: "lør. 5.
     * marts".
     */
    WEEKDAY_SHORT_DATE_LONG("EEE d. MMMM"),

    /**
     * Show date according to pattern "EEE 'd.'d MMMM yyyy". Example:
     * "lør. d.5 marts 2022".
     */
    WEEKDAY_SHORT_DATE_D_LONG_YEAR("EEE 'd.'d MMMM yyyy"),

    /**
     * Show date according to pattern "EEEE d. MMMM". Example: "lørdag
     * 5. marts".
     */
    WEEKDAY_LONG_DATE_LONG("EEEE d. MMMM"),

    /**
     * Show date according to pattern "EEEE 'd.'d MMMM yyyy". Example:
     * "lørdag d.5 marts 2022".
     */
    WEEKDAY_LONG_DATE_D_LONG_YEAR("EEEE 'd.'d MMMM yyyy"),

    /**
     * Returns the iso week number according to pattern "ww". Example:
     * "07".
     */
    ISOWEEK_TWODIGIT("ww"),

    /**
     * Returns the iso week number according to pattern "w". Example:
     * "7".
     */
    ISOWEEK_NATURAL("w"),

    /**
     * Show time according to pattern "HH:mm:ss". Example: "13:50:50".
     */
    TIME_SECONDS("HH:mm:ss"),

    /** Show time according to pattern "HH:mm". Example: "13:50". */
    TIME("HH:mm"),

    /**
     * Show date and time according to pattern "yyyy-MM-dd HH:mm:ss".
     * Example: "2020-08-23 13:50:38".
     */
    TIMESTAMP("yyyy-MM-dd HH:mm:ss")
}

enum class DateComponent(val calendarComponent: Int) {
    YEAR(Calendar.YEAR),
    MONTH(Calendar.MONTH),
    DAY(Calendar.DAY_OF_MONTH),
    HOUR(Calendar.HOUR_OF_DAY),
    MINUTE(Calendar.MINUTE),
    SECOND(Calendar.SECOND),
    WEEKDAY(Calendar.DAY_OF_WEEK),
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
open class MDate(val calendar: Calendar = Calendar.getInstance(), private val locale: Locale) :
    Comparable<MDate> {
    val year get() = calendar.get(Calendar.YEAR)
    val month get() = calendar.get(Calendar.MONTH) + 1
    val day get() = calendar.get(Calendar.DAY_OF_MONTH)
    val hour get() = calendar.get(Calendar.HOUR_OF_DAY)
    val minute get() = calendar.get(Calendar.MINUTE)
    val second get() = calendar.get(Calendar.SECOND)
    val dayOfWeek get() = calendar.get(Calendar.DAY_OF_WEEK)

    /**
     * Show date or time according to provided format. Only PrettyDate
     * formats use the caseType parameter.
     *
     * @param format Format of the MDate as either date or time. See
     *     MDateFormat kdocs for the individual formats.
     * @param caseType Casing/capitalization of month/day for pretty
     *     formats. Default: LowerCase
     * @param showAbbreviatedPeriod For abbreviated month/day show a
     *     period after the name. This has only been
     *     tested in DK localization. Default: true
     * @return
     */
    fun show(
        format: MDateFormat,
        caseType: CaseType = CaseType.LowerCase,
        showAbbreviatedPeriod: Boolean = true
    ): String {
        val dateFormat = SimpleDateFormat(format.pattern, locale)
        val casedDate = when (format) {
            PRETTYDATE_LONG, PRETTYDATE_SHORT, PRETTYDATE_YEAR_LONG, PRETTYDATE_YEAR_SHORT,
            WEEKDAY_SHORT_DATE_LONG, WEEKDAY_SHORT_DATE_D_LONG_YEAR,
            WEEKDAY_LONG_DATE_LONG, WEEKDAY_LONG_DATE_D_LONG_YEAR -> dateFormat.format(
                calendar.time
            ).case(caseType, locale = locale)
            PRETTYDATE_VERYSHORT, PRETTYDATE_YEAR_VERYSHORT -> {
                val dateParts = dateFormat.format(calendar.time).split(".")
                val formattedDateParts = dateParts.mapIndexed { index, part ->
                    if (index == 1) { // Month
                        part.case(caseType, locale = locale)
                    } else { // Day
                        part
                    }
                }
                formattedDateParts.joinToString(".")
            }
            else -> dateFormat.format(calendar.time)
        }
        if (showAbbreviatedPeriod.not()) {
            val periodedDate = when (format) {
                WEEKDAY_SHORT_DATE_LONG, WEEKDAY_SHORT_DATE_D_LONG_YEAR -> {
                    // Remove first period
                    val posOfPeriod = casedDate.indexOfFirst { char -> char == '.' }
                    casedDate.removeRange(posOfPeriod, posOfPeriod + 1)
                }
                PRETTYDATE_SHORT, PRETTYDATE_VERYSHORT, PRETTYDATE_YEAR_SHORT,
                PRETTYDATE_YEAR_VERYSHORT -> {
                    // Remove last period
                    val posOfPeriod = casedDate.indexOfLast { char -> char == '.' }
                    casedDate.removeRange(posOfPeriod, posOfPeriod + 1)
                }
                else -> casedDate
            }
            return periodedDate
        }
        return casedDate
    }

    /**
     * Show datetime according to provided pattern
     *
     * @param pattern Pattern of the MDate. See SimpleDateFormat for the
     *     individual component:
     *     https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html.
     * @param caseType Casing/capitalization of month/day for patterns
     *     where they are written out. Default: LowerCase
     * @param mapper Optional mapper to further manipulate the output.
     * @return
     */
    fun show(
        pattern: String,
        caseType: CaseType = CaseType.LowerCase,
        mapper: ((String) -> String)? = null
    ): String {
        val formattedDate = SimpleDateFormat(pattern, locale).format(calendar.time)
        val casedDate = formattedDate.case(caseType, locale = locale)
        return mapper?.invoke(casedDate) ?: casedDate
    }

    //
    // Date Manipulation functions
    //

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

    fun clone(
        year: Int = this.year,
        month: Int = this.month,
        day: Int = this.day,
        hour: Int = this.hour,
        minute: Int = this.minute,
        second: Int = this.second
    ): MDate {
        val newCal = calendar.clone() as Calendar
        newCal.set(Calendar.YEAR, year)
        newCal.set(Calendar.MONTH, month - 1)
        newCal.set(Calendar.DAY_OF_MONTH, day)
        newCal.set(Calendar.HOUR_OF_DAY, hour)
        newCal.set(Calendar.MINUTE, minute)
        newCal.set(Calendar.SECOND, second)
        return MDate(newCal, locale)
    }

    /**
     * Get whether the MDate is "now", using BuilderDk().now() to find
     * the current datetime.
     *
     * @param scale Which level of time that needs to match to be
     *     considered "now". I.E. If scale is set to
     *     DateComponent.MONTH, and the current date is January 22.
     *     2022, then any datetime in January 2022 would be return
     *     true. January 2021 or January 2023 would return false.
     * @return True if MDate is "now", false otherwise
     */
    fun isNow(scale: DateComponent = DateComponent.DAY): Boolean {
        val today = BuilderDk().now()
        return equals(today, scale)
    }

    fun isYesterday(): Boolean {
        return this.plusDays(1).isToday()
    }

    fun isToday(): Boolean {
        return isNow(DateComponent.DAY)
    }

    fun isTomorrow(): Boolean {
        return this.plusDays(-1).isToday()
    }

    //
    // Comparison functions
    //

    /**
     * Returns the difference in discreet time units between two dates.
     * Example: this = 2020 24 december 23:55 and param date = 2022
     * 26 december 01:00 Results for YEAR = 2 or MONTH = 24 Example:
     * this = 2022 24 december 23:55 and param date = 2022 26 december
     * 01:00 DAY = 2 or HOUR = 26 Example: this = 2022 26 december
     * 23:55:55 and param date = 2022 26 december 01:00:00 MINUTE = 65
     *
     * @param date
     * @param scale
     * @return
     */
    fun timeUntil(date: MDate, scale: DateComponent = DateComponent.SECOND): Int {
        if (scale == DateComponent.SECOND) {
            return ((date.calendar.timeInMillis - calendar.timeInMillis) / 1000).toInt()
        }

        val yearDif = date.year - this.year
        if (scale == DateComponent.YEAR) {
            return yearDif
        }
        val monthDif = yearDif * 12 + date.month - this.month
        if (scale == DateComponent.MONTH) {
            return monthDif
        }

        val jodaDate = LocalDate.fromCalendarFields(date.calendar)
        val jodaThis = LocalDate.fromCalendarFields(this.calendar)
        val daysDif = Days.daysBetween(jodaThis, jodaDate).days
        // We use daysBetween because that is the easiest way to account for multiple-month spans
        // (where different months will have different lengths. We let Joda handle that)
        if (scale == DateComponent.DAY) {
            return daysDif
        }

        val hoursDif = daysDif * 24 + date.hour - this.hour
        // I.E. 24 december 23:00 and 26 december 01:00 got us a daysDif = "2" (see daysDif comment)
        // 01:00 minus 23:00 will give us -22 hours.
        // 48 hours (2 days) - 22 hours = 26 hours (correct result).
        if (scale == DateComponent.HOUR) {
            return hoursDif
        }

        val minutesDif = hoursDif * 60 + date.minute - this.minute
        if (scale == DateComponent.MINUTE) {
            return minutesDif
        }
        throw NotImplementedError("$scale is not implemented in function")
    }

    /**
     * Get whether two MDates equal each other.
     *
     * @param scale Which level of time that needs to match to be
     *     considered "now". Calendar.YEAR, Calendar.MONTH,
     *     Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY,
     *     Calendar.MINUTE. I.E. If scale is set to Calendar.MONTH,
     *     and the current date is January 22. 2022, then any
     *     datetime in January 2022 would be return true.
     *     January 2021 or January 2023 would return false.
     * @return True if the dates are equal at the given scale, false
     *     otherwise
     */
    fun equals(date: MDate, scale: DateComponent = DateComponent.SECOND): Boolean {
        if (scale == DateComponent.SECOND) {
            return date == this
        }

        var isEqual = this.year == date.year
        if (scale == DateComponent.YEAR) {
            return isEqual
        }
        isEqual = isEqual && this.month == date.month
        if (scale == DateComponent.MONTH) {
            return isEqual
        }
        isEqual = isEqual && this.day == date.day
        if (scale == DateComponent.DAY) {
            return isEqual
        }
        isEqual = isEqual && this.hour == date.hour
        if (scale == DateComponent.HOUR) {
            return isEqual
        }
        isEqual = isEqual && this.minute == date.minute
        if (scale == DateComponent.MINUTE) {
            return isEqual
        }
        throw NotImplementedError("$scale is not implemnted in function")
    }

    override fun equals(other: Any?): Boolean {
        if (other !is MDate)
            return false
        return compareTo(other) == 0
    }

    override fun compareTo(other: MDate): Int {
        val result = ((this.calendar.timeInMillis - other.calendar.timeInMillis) / 1000).toInt()
        return result
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

    override fun toString(): String {
        return "$year $month/$day $hour:$minute:$second"
    }

    override fun hashCode(): Int {
        return calendar.hashCode()
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
 * Use now() without any setters to acquire an MDate instance with the
 * current datetime
 *
 * @property locale The localization that should be used by the MDate
 *     instance. Defaults to the device's default locale.
 */
class MDateBuilder(private val locale: Locale = Locale.getDefault()) {
    private var year: Int = 2000
    private var month: Int = 1
    private var day: Int = 1
    private var hour: Int = 0
    private var minute: Int = 0
    private var second: Int = 0

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

    fun parse(string: String, pattern: String, patternLocale: Locale = Locale.ENGLISH): MDate {
        val dateFormat = SimpleDateFormat(pattern, patternLocale)
        val jDate = dateFormat.parse(string) ?: throw IllegalArgumentException("date parser returned null")
        return MDate.BuilderDk().fromJavaDate(jDate)
    }

    fun fromJavaDate(date: Date): MDate {
        val calendar = Calendar.getInstance(locale)
        calendar.timeInMillis = date.time
        return MDate(calendar, locale)
    }

    fun now(): MDate {
        val currentDateTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance(locale)
        calendar.timeInMillis = currentDateTime
        return MDate(calendar, locale)
    }

    fun build(): MDate {
        val calendar = Calendar.getInstance(locale)
        calendar.set(year, month - 1, day, hour, minute, second)
        calendar.set(Calendar.MILLISECOND, 0)
        return MDate(calendar, locale)
    }
}
