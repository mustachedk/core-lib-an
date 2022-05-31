package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.MDate
import org.junit.Assert

import org.junit.Test

class MDateOtherTest {

    @Test
    fun roundToDateFromLowTime() {
        // Arrange
        val testDay = 5
        val testMonth = 6
        val testYear = 2000

        val date = MDate.BuilderDk().dateTime(year = testYear, month = testMonth, day = testDay, hour = 3, minute = 5, second = 59).build()

        // Act
        val newDate = date.roundToDate()

        // Assert
        Assert.assertEquals(0, newDate.second)
        Assert.assertEquals(0, newDate.minute)
        Assert.assertEquals(0, newDate.hour)
        Assert.assertEquals(testDay, newDate.day)
        Assert.assertEquals(testMonth, newDate.month)
        Assert.assertEquals(testYear, newDate.year)
    }

    @Test
    fun roundToDateFromHighTime() {
        // Arrange
        val testDay = 5
        val testMonth = 6
        val testYear = 2000

        val date = MDate.BuilderDk().dateTime(year = testYear, month = testMonth, day = testDay, hour = 23, minute = 59, second = 59).build()

        // Act
        val newDate = date.roundToDate()

        // Assert
        Assert.assertEquals(0, newDate.second)
        Assert.assertEquals(0, newDate.minute)
        Assert.assertEquals(0, newDate.hour)
        Assert.assertEquals(testDay, newDate.day)
        Assert.assertEquals(testMonth, newDate.month)
        Assert.assertEquals(testYear, newDate.year)
    }

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