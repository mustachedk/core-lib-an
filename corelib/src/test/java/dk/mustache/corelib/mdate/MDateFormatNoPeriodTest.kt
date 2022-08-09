package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.MDate
import dk.mustache.corelib.utils.MDateFormat
import org.junit.Assert
import org.junit.Test

class MDateFormatNoPeriodTest {
    @Test
    fun formatWeekDayShortDateLong() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 8).build()
        val expectedOutput = "man 8. august"

        // Act
        val newDate = date.show(MDateFormat.WEEKDAY_SHORT_DATE_LONG, showAbbreviatedPeriod = false)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatWeekDayShortDateDLongYear() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 8).build()
        val expectedOutput = "man d.8 august 2022"

        // Act
        val newDate = date.show(MDateFormat.WEEKDAY_SHORT_DATE_D_LONG_YEAR, showAbbreviatedPeriod = false)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateShort() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 8).build()
        val expectedOutput = "8. aug"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_SHORT, showAbbreviatedPeriod = false)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateVeryShort() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 8).build()
        val expectedOutput = "8.aug"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_VERYSHORT, showAbbreviatedPeriod = false)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateYearShort() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 8).build()
        val expectedOutput = "8. aug 2022"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_YEAR_SHORT, showAbbreviatedPeriod = false)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateYearVeryShort() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 8).build()
        val expectedOutput = "8.aug 22"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_YEAR_VERYSHORT, showAbbreviatedPeriod = false)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }
}