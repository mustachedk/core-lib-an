package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.MDate
import org.junit.Assert

import org.junit.Test

class MDateRoundingTest {

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


}