package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.MDate
import org.junit.Assert
import org.junit.Test

class MDateCalcTest {

    @Test
    fun addYears() {
        // Arrange
        val testYear = 2042
        val date = MDate.BuilderDk().date(year = testYear).build()

        // Act
        val newDate = date.plusDate(years = 3)

        // Assert
        Assert.assertEquals(testYear, date.year)
        Assert.assertEquals(testYear + 3, newDate.year)
    }

    @Test
    fun subtractYears() {
        // Arrange
        val testYear = 2042
        val date = MDate.BuilderDk().date(year = testYear).build()

        // Act
        val newDate = date.plusDate(years = -3)

        // Assert
        Assert.assertEquals(testYear, date.year)
        Assert.assertEquals(testYear - 3, newDate.year)
    }

    @Test
    fun addMonths() {
        // Arrange
        val testMonth = 7
        val date = MDate.BuilderDk().date(month = testMonth).build()

        // Act
        val newDate = date.plusMonths(3)

        // Assert
        Assert.assertEquals(testMonth, date.month)
        Assert.assertEquals(testMonth + 3, newDate.month)
    }

    @Test
    fun subtractMonths() {
        // Arrange
        val testMonth = 7
        val date = MDate.BuilderDk().date(month = testMonth).build()

        // Act
        val newDate = date.plusMonths(-3)

        // Assert
        Assert.assertEquals(testMonth, date.month)
        Assert.assertEquals(testMonth - 3, newDate.month)
    }

    @Test
    fun addMonthsPastYearEnd() {
        // Arrange
        val testMonth = 11
        val testYear = 2000
        val date = MDate.BuilderDk().date(year = testYear, month = testMonth).build()

        // Act
        val newDate = date.plusMonths(3)

        // Assert
        Assert.assertEquals(testMonth, date.month)
        Assert.assertEquals((testMonth + 3) - 12, newDate.month)
        Assert.assertEquals(testYear + 1 , newDate.year)
    }

    @Test
    fun subtractMonthsPastYearStart() {
        // Arrange
        val testMonth = 1
        val testYear = 2000
        val date = MDate.BuilderDk().date(year = testYear, month = testMonth).build()

        // Act
        val newDate = date.plusMonths(-3)

        // Assert
        Assert.assertEquals(testMonth, date.month)
        Assert.assertEquals((testMonth - 3) + 12, newDate.month)
        Assert.assertEquals(testYear - 1 , newDate.year)
    }

    @Test
    fun addDays() {
        // Arrange
        val testDay = 7
        val date = MDate.BuilderDk().date(day = testDay).build()

        // Act
        val newDate = date.plusDays(3)

        // Assert
        Assert.assertEquals(testDay, date.day)
        Assert.assertEquals(testDay + 3, newDate.day)
    }

    @Test
    fun subtractDays() {
        // Arrange
        val testDay = 7
        val date = MDate.BuilderDk().date(day = testDay).build()

        // Act
        val newDate = date.plusDays(-3)

        // Assert
        Assert.assertEquals(testDay, date.day)
        Assert.assertEquals(testDay - 3, newDate.day)
    }

    @Test
    fun addDaysPastYearEnd() {
        // Arrange
        val testDay = 28
        val testMonth = 12
        val testYear = 2000
        // 28th december + 5 == 2 january
        val expectedDay = 2
        val expectedMonth = 1
        val date = MDate.BuilderDk().date(year = testYear, month = testMonth, day = testDay).build()

        // Act
        val newDate = date.plusDays(5)

        // Assert
        Assert.assertEquals(testDay, date.day)
        Assert.assertEquals(expectedDay, newDate.day)
        Assert.assertEquals(expectedMonth, newDate.month)
        Assert.assertEquals(testYear + 1 , newDate.year)
    }

    @Test
    fun subtractDaysPastYearStart() {
        // Arrange
        val testDay = 2
        val testMonth = 1
        val testYear = 2000
        // 2nd january - 5 == 28 december
        val expectedDay = 28
        val expectedMonth = 12
        val date = MDate.BuilderDk().date(year = testYear, month = testMonth, day = testDay).build()

        // Act
        val newDate = date.plusDays(-5)

        // Assert
        Assert.assertEquals(testDay, date.day)
        Assert.assertEquals(expectedDay, newDate.day)
        Assert.assertEquals(expectedMonth, newDate.month)
        Assert.assertEquals(testYear - 1 , newDate.year)
    }

    @Test
    fun addHours() {
        // Arrange
        val testHour = 7
        val date = MDate.BuilderDk().time(hour = testHour).build()

        // Act
        val newDate = date.plusHours(3)

        // Assert
        Assert.assertEquals(testHour, date.hour)
        Assert.assertEquals(testHour + 3, newDate.hour)
    }

    @Test
    fun subtractHours() {
        // Arrange
        val testHour = 7
        val date = MDate.BuilderDk().time(hour = testHour).build()

        // Act
        val newDate = date.plusHours(-3)

        // Assert
        Assert.assertEquals(testHour, date.hour)
        Assert.assertEquals(testHour - 3, newDate.hour)
    }

    @Test
    fun addHoursPastYearEnd() {
        // Arrange
        val testHour = 22
        val testDay = 31
        val testMonth = 12
        val testYear = 2000

        val expectedHour = 3
        val expectedDay = 1
        val expectedMonth = 1

        val date = MDate.BuilderDk().dateTime(year = testYear, month = testMonth, day = testDay, hour = testHour).build()

        // Act
        val newDate = date.plusHours(5)

        // Assert
        Assert.assertEquals(testHour, date.hour)
        Assert.assertEquals(expectedHour, newDate.hour)
        Assert.assertEquals(expectedDay, newDate.day)
        Assert.assertEquals(expectedMonth, newDate.month)
        Assert.assertEquals(testYear + 1 , newDate.year)
    }

    @Test
    fun subtractHoursPastYearStart() {
        // Arrange
        val testHour = 2
        val testDay = 1
        val testMonth = 1
        val testYear = 2000

        val expectedHour = 21
        val expectedDay = 31
        val expectedMonth = 12

        val date = MDate.BuilderDk().dateTime(year = testYear, month = testMonth, day = testDay, hour = testHour).build()

        // Act
        val newDate = date.plusHours(-5)

        // Assert
        Assert.assertEquals(testHour, date.hour)
        Assert.assertEquals(expectedHour, newDate.hour)
        Assert.assertEquals(expectedDay, newDate.day)
        Assert.assertEquals(expectedMonth, newDate.month)
        Assert.assertEquals(testYear - 1 , newDate.year)
    }

    @Test
    fun addMinutes() {
        // Arrange
        val testMinute = 7
        val date = MDate.BuilderDk().time(minute = testMinute).build()

        // Act
        val newDate = date.plusMinutes(3)

        // Assert
        Assert.assertEquals(testMinute, date.minute)
        Assert.assertEquals(testMinute + 3, newDate.minute)
    }

    @Test
    fun subtractMinutes() {
        // Arrange
        val testMinute = 7
        val date = MDate.BuilderDk().time(minute = testMinute).build()

        // Act
        val newDate = date.plusMinutes(-3)

        // Assert
        Assert.assertEquals(testMinute, date.minute)
        Assert.assertEquals(testMinute - 3, newDate.minute)
    }

    @Test
    fun addMinutesPastYearEnd() {
        // Arrange
        val testMinute = 58
        val testHour = 23
        val testDay = 31
        val testMonth = 12
        val testYear = 2000

        val expectedMinute = 3
        val expectedHour = 0
        val expectedDay = 1
        val expectedMonth = 1

        val date = MDate.BuilderDk().dateTime(year = testYear, month = testMonth, day = testDay, hour = testHour, minute = testMinute).build()

        // Act
        val newDate = date.plusMinutes(5)

        // Assert
        Assert.assertEquals(testMinute, date.minute)
        Assert.assertEquals(expectedMinute, newDate.minute)
        Assert.assertEquals(expectedHour, newDate.hour)
        Assert.assertEquals(expectedDay, newDate.day)
        Assert.assertEquals(expectedMonth, newDate.month)
        Assert.assertEquals(testYear + 1 , newDate.year)
    }

    @Test
    fun subtractMinutesPastYearStart() {
        // Arrange
        val testMinute = 3
        val testHour = 0
        val testDay = 1
        val testMonth = 1
        val testYear = 2000

        val expectedMinute = 58
        val expectedHour = 23
        val expectedDay = 31
        val expectedMonth = 12

        val date = MDate.BuilderDk().dateTime(year = testYear, month = testMonth, day = testDay, hour = testHour, minute = testMinute).build()

        // Act
        val newDate = date.plusMinutes(-5)

        // Assert
        Assert.assertEquals(testMinute, date.minute)
        Assert.assertEquals(expectedMinute, newDate.minute)
        Assert.assertEquals(expectedHour, newDate.hour)
        Assert.assertEquals(expectedDay, newDate.day)
        Assert.assertEquals(expectedMonth, newDate.month)
        Assert.assertEquals(testYear - 1 , newDate.year)
    }

    @Test
    fun addSeconds() {
        // Arrange
        val testSecond = 7
        val date = MDate.BuilderDk().time(second = testSecond).build()

        // Act
        val newDate = date.plusSeconds(3)

        // Assert
        Assert.assertEquals(testSecond, date.second)
        Assert.assertEquals(testSecond + 3, newDate.second)
    }

    @Test
    fun subtractSeconds() {
        // Arrange
        val testSecond = 7
        val date = MDate.BuilderDk().time(second = testSecond).build()

        // Act
        val newDate = date.plusSeconds(-3)

        // Assert
        Assert.assertEquals(testSecond, date.second)
        Assert.assertEquals(testSecond - 3, newDate.second)
    }

    @Test
    fun addSecondsPastYearEnd() {
        // Arrange
        val testSecond = 58
        val testMinute = 59
        val testHour = 23
        val testDay = 31
        val testMonth = 12
        val testYear = 2000

        val expectedSecond = 3
        val expectedMinute = 0
        val expectedHour = 0
        val expectedDay = 1
        val expectedMonth = 1

        val date = MDate.BuilderDk().dateTime(year = testYear, month = testMonth, day = testDay, hour = testHour, minute = testMinute, second = testSecond).build()

        // Act
        val newDate = date.plusSeconds(5)

        // Assert
        Assert.assertEquals(testSecond, date.second)
        Assert.assertEquals(expectedSecond, newDate.second)
        Assert.assertEquals(expectedMinute, newDate.minute)
        Assert.assertEquals(expectedHour, newDate.hour)
        Assert.assertEquals(expectedDay, newDate.day)
        Assert.assertEquals(expectedMonth, newDate.month)
        Assert.assertEquals(testYear + 1 , newDate.year)
    }

    @Test
    fun subtractSecondsPastYearStart() {
        // Arrange
        val testSecond = 3
        val testMinute = 0
        val testHour = 0
        val testDay = 1
        val testMonth = 1
        val testYear = 2000

        val expectedSecond = 58
        val expectedMinute = 59
        val expectedHour = 23
        val expectedDay = 31
        val expectedMonth = 12

        val date = MDate.BuilderDk().dateTime(year = testYear, month = testMonth, day = testDay, hour = testHour, minute = testMinute, second = testSecond).build()

        // Act
        val newDate = date.plusSeconds(-5)

        // Assert
        Assert.assertEquals(testSecond, date.second)
        Assert.assertEquals(expectedSecond, newDate.second)
        Assert.assertEquals(expectedMinute, newDate.minute)
        Assert.assertEquals(expectedHour, newDate.hour)
        Assert.assertEquals(expectedDay, newDate.day)
        Assert.assertEquals(expectedMonth, newDate.month)
        Assert.assertEquals(testYear - 1 , newDate.year)
    }

    @Test
    fun addDate() {
        // Arrange
        val testDay = 2
        val testMonth = 1

        val addDay = 1
        val addMonth = 6
        // 2nd january -> 3rd july
        val expectedDay = 3
        val expectedMonth = 7
        val date = MDate.BuilderDk().date(month = testMonth, day = testDay).build()

        // Act
        val newDate = date.plusDate(months = addMonth, days = addDay)

        // Assert
        Assert.assertEquals(expectedMonth, newDate.month)
        Assert.assertEquals(expectedDay, newDate.day)
    }

    @Test
    fun subtractDate() {
        // Arrange
        val testDay = 3
        val testMonth = 7

        val subDay = -1
        val subMonth = -6
        // 3rd july -> 2nd january
        val expectedDay = 2
        val expectedMonth = 1
        val date = MDate.BuilderDk().date(month = testMonth, day = testDay).build()

        // Act
        val newDate = date.plusDate(months = subMonth, days = subDay)

        // Assert
        Assert.assertEquals(expectedMonth, newDate.month)
        Assert.assertEquals(expectedDay, newDate.day)
    }

    @Test
    fun addTime() {
        // Arrange
        val testMinute = 15
        val testHour = 12

        val addMinute = 30
        val addHour = 3
        // 12:15 -> 15:45
        val expectedMinute = 45
        val expectedHour = 15

        val date = MDate.BuilderDk().dateTime(hour = testHour, minute = testMinute).build()

        // Act
        val newDate = date.plusTime(hours = addHour, minutes = addMinute)

        // Assert
        Assert.assertEquals(testMinute, date.minute)
        Assert.assertEquals(expectedMinute, newDate.minute)
        Assert.assertEquals(expectedHour, newDate.hour)
    }

    @Test
    fun subtractTime() {
        // Arrange
        val testMinute = 15
        val testHour = 45

        val subMinute = -3
        val subHour = -30
        // 15:45 -> 12:15
        val expectedMinute = 12
        val expectedHour = 15

        val date = MDate.BuilderDk().dateTime(hour = testHour, minute = testMinute).build()

        // Act
        val newDate = date.plusTime(hours = subHour, minutes = subMinute)

        // Assert
        Assert.assertEquals(testMinute, date.minute)
        Assert.assertEquals(expectedMinute, newDate.minute)
        Assert.assertEquals(expectedHour, newDate.hour)
    }

    @Test
    fun addDateTime() {
        // Arrange
        val testMinute = 15
        val testHour = 12
        val testDay = 2
        val testMonth = 1

        val addMinute = 30
        val addHour = 3
        val addDay = 1
        val addMonth = 6
        // 2nd january -> 3rd july
        // 12:15 -> 15:45
        val expectedMinute = 45
        val expectedHour = 15
        val expectedDay = 3
        val expectedMonth = 7

        val date = MDate.BuilderDk().dateTime(month = testMonth, day = testDay, hour = testHour, minute = testMinute).build()

        // Act
        val newDate = date.plusDateTime(months = addMonth, days = addDay, hours = addHour, minutes = addMinute)

        // Assert
        Assert.assertEquals(testMinute, date.minute)
        Assert.assertEquals(expectedMinute, newDate.minute)
        Assert.assertEquals(expectedHour, newDate.hour)
        Assert.assertEquals(expectedDay, newDate.day)
        Assert.assertEquals(expectedMonth, newDate.month)
    }

    @Test
    fun subtractDateTime() {
        // Arrange
        val testMinute = 15
        val testHour = 45
        val testDay = 3
        val testMonth = 7

        val subMinute = -3
        val subHour = -30
        val subDay = -1
        val subMonth = -6
        // 3rd july -> 2nd january
        // 15:45 -> 12:15
        val expectedMinute = 12
        val expectedHour = 15
        val expectedDay = 2
        val expectedMonth = 1

        val date = MDate.BuilderDk().dateTime(month = testMonth, day = testDay, hour = testHour, minute = testMinute).build()

        // Act
        val newDate = date.plusDateTime(months = subMonth, days = subDay, hours = subHour, minutes = subMinute)

        // Assert
        Assert.assertEquals(testMinute, date.minute)
        Assert.assertEquals(expectedMinute, newDate.minute)
        Assert.assertEquals(expectedHour, newDate.hour)
        Assert.assertEquals(expectedDay, newDate.day)
        Assert.assertEquals(expectedMonth, newDate.month)
    }
}