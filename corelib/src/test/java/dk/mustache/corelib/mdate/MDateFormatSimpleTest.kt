package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.MDate
import dk.mustache.corelib.utils.MDateFormat.*
import org.junit.Assert
import org.junit.Test

class MDateFormatSimpleTest {
    @Test
    fun formatDateYear() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 21).build()
        val expectedOutput = "21/06 2000"

        // Act
        val newDate = date.show(DATE_YEAR)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatDateYearReverse() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 21).build()
        val expectedOutput = "2000-06-21"

        // Act
        val newDate = date.show(DATE_YEAR_REVERSE)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatDateYearShort() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 21).build()
        val expectedOutput = "21/6 2000"

        // Act
        val newDate = date.show(DATE_YEAR_SHORT)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatDate() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(month = 6, day = 21).build()
        val expectedOutput = "21/06"

        // Act
        val newDate = date.show(DATE)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatDateShort() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(month = 6, day = 21).build()
        val expectedOutput = "21/6"

        // Act
        val newDate = date.show(DATE_SHORT)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatTimeSeconds() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(hour = 5, minute = 32, second = 58).build()
        val expectedOutput = "05:32:58"

        // Act
        val newDate = date.show(TIME_SECONDS)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatTime() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(hour = 5, minute = 32).build()
        val expectedOutput = "05:32"

        // Act
        val newDate = date.show(TIME)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }
}