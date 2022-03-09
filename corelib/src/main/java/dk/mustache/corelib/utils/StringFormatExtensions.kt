package dk.mustache.corelib.utils

import java.util.*

fun String.capitalizeWords(locale: Locale = Locale.getDefault()): String =
    split(" ").map { it.capitalize(locale) }.joinToString(" ")

/**
 * Returns a copy of this string transformed according to the variables
 * given.
 *
 * UpperCase -> All letters are transformed to uppercase
 *
 * CapFirst -> First letter in the string is transformed to uppercase
 *
 * CapWords -> First letter in each word is transformed to uppercase
 *
 * LowerCase -> All letter are transformed to lowercase.
 *
 * Force first transforms the string to lowercase, so the only uppercase
 * will be those fitting the given caseType.
 *
 * @param caseType Choice between Uppercase, CapFirst, CapWords and
 *     LowerCase
 * @param force If true, string is transformed into lowercase before
 * @param locale
 *     casetype transformation
 */
fun String.case(
    caseType: CaseType,
    force: Boolean = false,
    locale: Locale = Locale.getDefault()
): String {
    return when (caseType) {
        CaseType.UpperCase -> this.toUpperCase(locale)
        CaseType.CapFirst -> {
            if (force) {
                this.toLowerCase(locale).capitalize(locale)
            } else {
                this.capitalize(locale)
            }
        }
        CaseType.CapWords -> {
            if (force) {
                this.toLowerCase(locale).capitalizeWords(locale)
            } else {
                this.capitalizeWords(locale)
            }
        }
        CaseType.LowerCase -> this.toLowerCase(locale)
        else -> this
    }

}

interface CaseType {
    object UpperCase : CaseType
    object CapFirst : CaseType
    object CapWords : CaseType
    object LowerCase : CaseType
}