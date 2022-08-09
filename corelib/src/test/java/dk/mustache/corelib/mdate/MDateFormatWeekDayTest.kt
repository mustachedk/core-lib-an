package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.MDate
import dk.mustache.corelib.utils.MDateFormat
import org.junit.Assert
import org.junit.Test

class MDateFormatWeekDayTest {
    @Test
    fun formatWeekDayMonday() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 8).build()
        val expectedOutput = "man. 8. august"

        // Act
        val newDate = date.show(MDateFormat.WEEKDAY_SHORT_DATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }
    @Test
    fun formatWeekDayTuesday() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 9).build()
        val expectedOutput = "tir. 9. august"

        // Act
        val newDate = date.show(MDateFormat.WEEKDAY_SHORT_DATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }
    @Test
    fun formatWeekDayWednesday() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 10).build()
        val expectedOutput = "ons. 10. august"

        // Act
        val newDate = date.show(MDateFormat.WEEKDAY_SHORT_DATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }
    @Test
    fun formatWeekDayThursday() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 11).build()
        val expectedOutput = "tor. 11. august"

        // Act
        val newDate = date.show(MDateFormat.WEEKDAY_SHORT_DATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }
    @Test
    fun formatWeekDayFriday() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 12).build()
        val expectedOutput = "fre. 12. august"

        // Act
        val newDate = date.show(MDateFormat.WEEKDAY_SHORT_DATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }
    @Test
    fun formatWeekDayLørday() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 13).build()
        val expectedOutput = "lør. 13. august"

        // Act
        val newDate = date.show(MDateFormat.WEEKDAY_SHORT_DATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }
    @Test
    fun formatWeekDaySunday() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 14).build()
        val expectedOutput = "søn. 14. august"

        // Act
        val newDate = date.show(MDateFormat.WEEKDAY_SHORT_DATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatWeekDayMondayLong() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 8).build()
        val expectedOutput = "mandag 8. august"

        // Act
        val newDate = date.show(MDateFormat.WEEKDAY_LONG_DATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }
    @Test
    fun formatWeekDayTuesdayLong() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 9).build()
        val expectedOutput = "tirsdag 9. august"

        // Act
        val newDate = date.show(MDateFormat.WEEKDAY_LONG_DATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }
    @Test
    fun formatWeekDayWednesdayLong() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 10).build()
        val expectedOutput = "onsdag 10. august"

        // Act
        val newDate = date.show(MDateFormat.WEEKDAY_LONG_DATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }
    @Test
    fun formatWeekDayThursdayLong() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 11).build()
        val expectedOutput = "torsdag 11. august"

        // Act
        val newDate = date.show(MDateFormat.WEEKDAY_LONG_DATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }
    @Test
    fun formatWeekDayFridayLong() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 12).build()
        val expectedOutput = "fredag 12. august"

        // Act
        val newDate = date.show(MDateFormat.WEEKDAY_LONG_DATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }
    @Test
    fun formatWeekDayLørdayLong() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 13).build()
        val expectedOutput = "lørdag 13. august"

        // Act
        val newDate = date.show(MDateFormat.WEEKDAY_LONG_DATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }
    @Test
    fun formatWeekDaySundayLong() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 14).build()
        val expectedOutput = "søndag 14. august"

        // Act
        val newDate = date.show(MDateFormat.WEEKDAY_LONG_DATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatWeekDayShortD() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 14).build()
        val expectedOutput = "søn. d.14 august 2022"

        // Act
        val newDate = date.show(MDateFormat.WEEKDAY_SHORT_DATE_D_LONG_YEAR)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatWeekDayLongD() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2022, month = 8, day = 14).build()
        val expectedOutput = "søndag d.14 august 2022"

        // Act
        val newDate = date.show(MDateFormat.WEEKDAY_LONG_DATE_D_LONG_YEAR)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }
}