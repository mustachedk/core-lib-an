package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.MDate
import dk.mustache.corelib.utils.MDateFormat.*
import org.junit.Assert
import org.junit.Test

class MDateFormatIsoWeekTest {
    @Test
    fun formatIsoWeekNatural() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2020, month = 1, day = 1).build()
        val expectedOutput = "1"

        // Act
        val newDate = date.show(ISOWEEK_NATURAL)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatIsoWeekTwoDigit() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2020, month = 1, day = 1).build()
        val expectedOutput = "01"

        // Act
        val newDate = date.show(ISOWEEK_TWODIGIT)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatIsoWeekFirstDayOfWeek() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 5, day = 30).build()
        val expectedOutput = "22"

        // Act
        val newDate = date.show(ISOWEEK_NATURAL)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatIsoWeekLastDayOfWeek() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 6, day = 5).build()
        val expectedOutput = "22"

        // Act
        val newDate = date.show(ISOWEEK_NATURAL)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatIsoWeekEdgecase() {
        //first couple of days of 2022 belong to week-year 2021

        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 1, day = 1).build()
        val expectedOutput = "52"

        // Act
        val newDate = date.show(ISOWEEK_TWODIGIT)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }
}