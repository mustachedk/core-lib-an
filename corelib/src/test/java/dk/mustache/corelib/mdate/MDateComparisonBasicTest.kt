package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.MDate
import org.junit.Assert
import org.junit.Test

class MDateComparisonBasicTest {
    @Test
    fun compareToEquals() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 8, hour = 11, minute = 15, second = 20).build()
        val date2 = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 8, hour = 11, minute = 15, second = 20).build()

        // Act
        val comparison = date1 == date2

        // Assert
        Assert.assertTrue(comparison)
    }

    @Test
    fun compareToLesserThan() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 8, hour = 11, minute = 15, second = 20).build()
        val date2 = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 8, hour = 11, minute = 15, second = 25).build()

        // Act
        val comparison = date1 < date2

        // Assert
        Assert.assertTrue(comparison)
    }

    @Test
    fun compareToGreaterThan() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 8, hour = 11, minute = 15, second = 30).build()
        val date2 = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 8, hour = 11, minute = 15, second = 25).build()

        // Act
        val comparison = date1 > date2

        // Assert
        Assert.assertTrue(comparison)
    }

    @Test
    fun compareToYear() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(year = 2002, month = 6, day = 8, hour = 11, minute = 15, second = 20).build()
        val date2 = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 8, hour = 11, minute = 15, second = 20).build()

        // Act
        val comparison = date1 > date2

        // Assert
        Assert.assertTrue(comparison)
    }

    @Test
    fun compareToMonth() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(year = 2000, month = 8, day = 8, hour = 11, minute = 15, second = 20).build()
        val date2 = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 8, hour = 11, minute = 15, second = 20).build()

        // Act
        val comparison = date1 > date2

        // Assert
        Assert.assertTrue(comparison)
    }

    @Test
    fun compareToDay() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 10, hour = 11, minute = 15, second = 20).build()
        val date2 = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 8, hour = 11, minute = 15, second = 20).build()

        // Act
        val comparison = date1 > date2

        // Assert
        Assert.assertTrue(comparison)
    }

    @Test
    fun compareToHour() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 8, hour = 13, minute = 15, second = 20).build()
        val date2 = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 8, hour = 11, minute = 15, second = 20).build()

        // Act
        val comparison = date1 > date2

        // Assert
        Assert.assertTrue(comparison)
    }

    @Test
    fun compareToMinute() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 8, hour = 11, minute = 17, second = 20).build()
        val date2 = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 8, hour = 11, minute = 15, second = 20).build()

        // Act
        val comparison = date1 > date2

        // Assert
        Assert.assertTrue(comparison)
    }

    @Test
    fun compareToSecond() {
        // Arrange
        val date1 = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 8, hour = 11, minute = 15, second = 22).build()
        val date2 = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 8, hour = 11, minute = 15, second = 20).build()

        // Act
        val comparison = date1 > date2

        // Assert
        Assert.assertTrue(comparison)
    }
}