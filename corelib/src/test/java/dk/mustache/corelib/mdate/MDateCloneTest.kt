package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.MDate
import org.junit.Assert
import org.junit.Test

class MDateCloneTest {
    @Test
    fun formatCloneRaw() {
        // Arrange
        val expectedYear = 2022
        val expectedMonth = 11
        val expectedDay = 11
        val expectedHour = 13
        val expectedMinute = 55
        val expectedSecond = 4

        val date = MDate.BuilderDk().dateTime(
            year = expectedYear,
            month = expectedMonth,
            day = expectedDay,
            hour = expectedHour,
            minute = expectedMinute,
            second = expectedSecond
        ).build()

        // Act
        val newDate = date.clone()

        // Assert
        Assert.assertEquals(expectedYear, newDate.year)
        Assert.assertEquals(expectedMonth, newDate.month)
        Assert.assertEquals(expectedDay, newDate.day)
        Assert.assertEquals(expectedHour, newDate.hour)
        Assert.assertEquals(expectedMinute, newDate.minute)
        Assert.assertEquals(expectedSecond, newDate.second)
    }

    @Test
    fun formatCloneWithNewYear() {
        // Arrange
        val expectedYear = 2020
        val expectedMonth = 11
        val expectedDay = 11
        val expectedHour = 13
        val expectedMinute = 55
        val expectedSecond = 4

        val date = MDate.BuilderDk().dateTime(
            year = 2022,
            month = expectedMonth,
            day = expectedDay,
            hour = expectedHour,
            minute = expectedMinute,
            second = expectedSecond
        ).build()

        // Act
        val newDate = date.clone(year = expectedYear)

        // Assert
        Assert.assertEquals(expectedYear, newDate.year)
        Assert.assertEquals(expectedMonth, newDate.month)
        Assert.assertEquals(expectedDay, newDate.day)
        Assert.assertEquals(expectedHour, newDate.hour)
        Assert.assertEquals(expectedMinute, newDate.minute)
        Assert.assertEquals(expectedSecond, newDate.second)
    }

    @Test
    fun formatCloneWithNewMonth() {
        // Arrange
        val expectedYear = 2022
        val expectedMonth = 11
        val expectedDay = 11
        val expectedHour = 13
        val expectedMinute = 55
        val expectedSecond = 4

        val date = MDate.BuilderDk().dateTime(
            year = expectedYear,
            month = 6,
            day = expectedDay,
            hour = expectedHour,
            minute = expectedMinute,
            second = expectedSecond
        ).build()

        // Act
        val newDate = date.clone(month = expectedMonth)

        // Assert
        Assert.assertEquals(expectedYear, newDate.year)
        Assert.assertEquals(expectedMonth, newDate.month)
        Assert.assertEquals(expectedDay, newDate.day)
        Assert.assertEquals(expectedHour, newDate.hour)
        Assert.assertEquals(expectedMinute, newDate.minute)
        Assert.assertEquals(expectedSecond, newDate.second)
    }

    @Test
    fun formatCloneWithNewDay() {
        // Arrange
        val expectedYear = 2022
        val expectedMonth = 11
        val expectedDay = 11
        val expectedHour = 13
        val expectedMinute = 55
        val expectedSecond = 4

        val date = MDate.BuilderDk().dateTime(
            year = expectedYear,
            month = expectedMonth,
            day = 9,
            hour = expectedHour,
            minute = expectedMinute,
            second = expectedSecond
        ).build()

        // Act
        val newDate = date.clone(day = expectedDay)

        // Assert
        Assert.assertEquals(expectedYear, newDate.year)
        Assert.assertEquals(expectedMonth, newDate.month)
        Assert.assertEquals(expectedDay, newDate.day)
        Assert.assertEquals(expectedHour, newDate.hour)
        Assert.assertEquals(expectedMinute, newDate.minute)
        Assert.assertEquals(expectedSecond, newDate.second)
    }

    @Test
    fun formatCloneWithNewHour() {
        // Arrange
        val expectedYear = 2022
        val expectedMonth = 11
        val expectedDay = 11
        val expectedHour = 13
        val expectedMinute = 55
        val expectedSecond = 4

        val date = MDate.BuilderDk().dateTime(
            year = expectedYear,
            month = expectedMonth,
            day = expectedDay,
            hour = 5,
            minute = expectedMinute,
            second = expectedSecond
        ).build()

        // Act
        val newDate = date.clone(hour = expectedHour)

        // Assert
        Assert.assertEquals(expectedYear, newDate.year)
        Assert.assertEquals(expectedMonth, newDate.month)
        Assert.assertEquals(expectedDay, newDate.day)
        Assert.assertEquals(expectedHour, newDate.hour)
        Assert.assertEquals(expectedMinute, newDate.minute)
        Assert.assertEquals(expectedSecond, newDate.second)
    }

    @Test
    fun formatCloneWithNewMinute() {
        // Arrange
        val expectedYear = 2022
        val expectedMonth = 11
        val expectedDay = 11
        val expectedHour = 13
        val expectedMinute = 55
        val expectedSecond = 4

        val date = MDate.BuilderDk().dateTime(
            year = expectedYear,
            month = expectedMonth,
            day = expectedDay,
            hour = expectedHour,
            minute = 50,
            second = expectedSecond
        ).build()

        // Act
        val newDate = date.clone(minute = expectedMinute)

        // Assert
        Assert.assertEquals(expectedYear, newDate.year)
        Assert.assertEquals(expectedMonth, newDate.month)
        Assert.assertEquals(expectedDay, newDate.day)
        Assert.assertEquals(expectedHour, newDate.hour)
        Assert.assertEquals(expectedMinute, newDate.minute)
        Assert.assertEquals(expectedSecond, newDate.second)
    }

    @Test
    fun formatCloneWithNewSecond() {
        // Arrange
        val expectedYear = 2022
        val expectedMonth = 11
        val expectedDay = 11
        val expectedHour = 13
        val expectedMinute = 55
        val expectedSecond = 4

        val date = MDate.BuilderDk().dateTime(
            year = expectedYear,
            month = expectedMonth,
            day = expectedDay,
            hour = expectedHour,
            minute = expectedMinute,
            second = 55
        ).build()

        // Act
        val newDate = date.clone(second = expectedSecond)

        // Assert
        Assert.assertEquals(expectedYear, newDate.year)
        Assert.assertEquals(expectedMonth, newDate.month)
        Assert.assertEquals(expectedDay, newDate.day)
        Assert.assertEquals(expectedHour, newDate.hour)
        Assert.assertEquals(expectedMinute, newDate.minute)
        Assert.assertEquals(expectedSecond, newDate.second)
    }

    @Test
    fun formatCloneWithNewMulti() {
        // Arrange
        val expectedYear = 2022
        val expectedMonth = 11
        val expectedDay = 11
        val expectedHour = 13
        val expectedMinute = 55
        val expectedSecond = 4

        val date = MDate.BuilderDk().dateTime(
            year = expectedYear,
            month = 5,
            day = expectedDay,
            hour = 23,
            minute = expectedMinute,
            second = 55
        ).build()

        // Act
        val newDate = date.clone(
            month = expectedMonth,
            hour = expectedHour,
            second = expectedSecond
        )

        // Assert
        Assert.assertEquals(expectedYear, newDate.year)
        Assert.assertEquals(expectedMonth, newDate.month)
        Assert.assertEquals(expectedDay, newDate.day)
        Assert.assertEquals(expectedHour, newDate.hour)
        Assert.assertEquals(expectedMinute, newDate.minute)
        Assert.assertEquals(expectedSecond, newDate.second)
    }
}