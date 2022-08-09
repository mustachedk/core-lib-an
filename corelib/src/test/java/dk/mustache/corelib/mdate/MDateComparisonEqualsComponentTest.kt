package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.DateComponent
import dk.mustache.corelib.utils.MDate
import org.junit.Assert
import org.junit.Test

class MDateComparisonEqualsComponentTest {
    @Test
    fun compareToYear() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(year = 2000, month = 6).build()
        val date2 = MDate.BuilderDk().dateTime(year = 2000, month = 8).build()

        // Act
        val comparison = date1.equals(date2, DateComponent.YEAR)

        // Assert
        Assert.assertTrue(comparison)
    }

    @Test
    fun compareToMonth() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(month = 8, day = 8).build()
        val date2 = MDate.BuilderDk().dateTime(month = 8, day = 30).build()

        // Act
        val comparison = date1.equals(date2, DateComponent.MONTH)

        // Assert
        Assert.assertTrue(comparison)
    }

    @Test
    fun compareToMonthFail() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(year = 2000, month = 8).build()
        val date2 = MDate.BuilderDk().dateTime(year = 2001, month = 8).build()

        // Act
        val comparison = date1.equals(date2, DateComponent.MONTH)

        // Assert
        Assert.assertFalse(comparison)
    }

    @Test
    fun compareToDay() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(day = 8, hour = 11).build()
        val date2 = MDate.BuilderDk().dateTime(day = 8, hour = 23).build()

        // Act
        val comparison = date1.equals(date2, DateComponent.DAY)

        // Assert
        Assert.assertTrue(comparison)
    }

    @Test
    fun compareToDayFail() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(month = 6, day = 8).build()
        val date2 = MDate.BuilderDk().dateTime(month = 7, day = 8).build()

        // Act
        val comparison = date1.equals(date2, DateComponent.DAY)

        // Assert
        Assert.assertFalse(comparison)
    }

    @Test
    fun compareToHour() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(hour = 11, minute = 55).build()
        val date2 = MDate.BuilderDk().dateTime(hour = 11, minute = 15).build()

        // Act
        val comparison = date1.equals(date2, DateComponent.HOUR)

        // Assert
        Assert.assertTrue(comparison)
    }

    @Test
    fun compareToHourFail() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(day = 8, hour = 11).build()
        val date2 = MDate.BuilderDk().dateTime(day = 9, hour = 11).build()

        // Act
        val comparison = date1.equals(date2, DateComponent.HOUR)

        // Assert
        Assert.assertFalse(comparison)
    }

    @Test
    fun compareToMinute() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(minute = 15, second = 55).build()
        val date2 = MDate.BuilderDk().dateTime(minute = 15, second = 20).build()

        // Act
        val comparison = date1.equals(date2, DateComponent.MINUTE)

        // Assert
        Assert.assertTrue(comparison)
    }

    @Test
    fun compareToMinuteFail() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(hour = 11, minute = 15).build()
        val date2 = MDate.BuilderDk().dateTime(hour = 13, minute = 15).build()

        // Act
        val comparison = date1.equals(date2, DateComponent.MINUTE)

        // Assert
        Assert.assertFalse(comparison)
    }

    @Test
    fun compareToSecond() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(second = 20).build()
        val date2 = MDate.BuilderDk().dateTime(second = 20).build()

        // Act
        val comparison = date1.equals(date2)

        // Assert
        Assert.assertTrue(comparison)
    }

    @Test
    fun compareToSecondFail() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(minute = 15, second = 20).build()
        val date2 = MDate.BuilderDk().dateTime(minute = 55, second = 20).build()

        // Act
        val comparison = date1.equals(date2)

        // Assert
        Assert.assertFalse(comparison)
    }
}