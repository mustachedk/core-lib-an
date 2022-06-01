package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.MDate
import org.junit.Assert
import org.junit.Test

class MDateBuilderTest {

    @Test
    fun buildDateTime() {
        // Arrange
        val testYear = 2000
        val testMonth = 6
        val testDay = 8
        val testHour = 11
        val testMinute = 15
        val testSecond = 21

        // Act
        val date = MDate.BuilderDk()
            .dateTime(testYear, testMonth, testDay, testHour, testMinute, testSecond).build()

        // Assert
        Assert.assertEquals(testYear, date.year)
        Assert.assertEquals(testMonth, date.month)
        Assert.assertEquals(testDay, date.day)
        Assert.assertEquals(testHour, date.hour)
        Assert.assertEquals(testMinute, date.minute)
        Assert.assertEquals(testSecond, date.second)
    }

    @Test
    fun buildOnlyDate() {
        // Arrange
        val testYear = 2000
        val testMonth = 6
        val testDay = 8

        // Act
        val date = MDate.BuilderDk()
            .date(testYear, testMonth, testDay).build()

        // Assert
        Assert.assertEquals(testYear, date.year)
        Assert.assertEquals(testMonth, date.month)
        Assert.assertEquals(testDay, date.day)
    }

    @Test
    fun buildOnlyTime() {
        // Arrange
        val testHour = 11
        val testMinute = 15
        val testSecond = 21

        // Act
        val date = MDate.BuilderDk()
            .time(testHour, testMinute, testSecond).build()

        // Assert
        Assert.assertEquals(testHour, date.hour)
        Assert.assertEquals(testMinute, date.minute)
        Assert.assertEquals(testSecond, date.second)
    }

    @Test
    fun buildOnlyYear() {
        // Arrange
        val testYear = 2000

        // Act
        val date = MDate.BuilderDk().year(testYear).build()

        // Assert
        Assert.assertEquals(testYear, date.year)
    }

    @Test
    fun buildOnlyMonth() {
        // Arrange
        val testMonth = 6

        // Act
        val date = MDate.BuilderDk().month(testMonth).build()

        // Assert
        Assert.assertEquals(testMonth, date.month)
    }

    @Test
    fun buildOnlyDay() {
        // Arrange
        val testDay = 6

        // Act
        val date = MDate.BuilderDk().day(testDay).build()

        // Assert
        Assert.assertEquals(testDay, date.day)
    }

    @Test
    fun buildOnlyHour() {
        // Arrange
        val testHour = 6

        // Act
        val date = MDate.BuilderDk().hour(testHour).build()

        // Assert
        Assert.assertEquals(testHour, date.hour)
    }

    @Test
    fun buildOnlyMinute() {
        // Arrange
        val testMinute = 6

        // Act
        val date = MDate.BuilderDk().minute(testMinute).build()

        // Assert
        Assert.assertEquals(testMinute, date.minute)
    }

    @Test
    fun buildOnlySecond() {
        // Arrange
        val testSecond = 6

        // Act
        val date = MDate.BuilderDk().second(testSecond).build()

        // Assert
        Assert.assertEquals(testSecond, date.second)
    }


    @Test
    fun buildAndReplaceMonth() {
        // Arrange
        val testMonthFirst = 6
        val testMonthSecond = 2

        // Act
        val date = MDate.BuilderDk()
            .dateTime(2000, testMonthFirst, 8, 11, 15, 21)
            .month(testMonthSecond)
            .build()


        // Assert
        Assert.assertEquals(testMonthSecond, date.month)
    }


    @Test
    fun buildNow() {
        // Arrange
        val expectedCurrentMillis = System.currentTimeMillis()

        // Act
        val date = MDate.BuilderDk().now()
        val actualMillis = date.calendar.timeInMillis

        // Assert
        // We can't match exact time, but we can determine we are within the right second
        Assert.assertTrue(expectedCurrentMillis - 500 < actualMillis)
        Assert.assertTrue(actualMillis < expectedCurrentMillis + 500)
    }
}