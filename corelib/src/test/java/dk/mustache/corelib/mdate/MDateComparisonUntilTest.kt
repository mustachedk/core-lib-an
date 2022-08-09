package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.DateComponent
import dk.mustache.corelib.utils.MDate
import org.junit.Assert
import org.junit.Test

class MDateComparisonUntilTest {
    @Test
    fun timeUntilYearLong() {
        // Arrange
        val from = MDate.BuilderDk().dateTime(2020, 1,1).build()
        val to = MDate.BuilderDk().dateTime(2022, 1,1).build()

        // Act
        val diff = from.timeUntil(to, DateComponent.YEAR)

        // Assert
        Assert.assertEquals(2, diff)
    }
    @Test
    fun timeUntilYearShort() {
        // Arrange
        val from = MDate.BuilderDk().dateTime(2020, 12,31).build()
        val to = MDate.BuilderDk().dateTime(2021, 1,1).build()

        // Act
        val diff = from.timeUntil(to, DateComponent.YEAR)

        // Assert
        Assert.assertEquals(1, diff)
    }

    @Test
    fun timeUntilMonthLong() {
        // Arrange
        val from = MDate.BuilderDk().dateTime(2020, 1,1).build()
        val to = MDate.BuilderDk().dateTime(2022, 1,1).build()

        // Act
        val diff = from.timeUntil(to, DateComponent.MONTH)

        // Assert
        Assert.assertEquals(24, diff)
    }
    @Test
    fun timeUntilMonthShort() {
        // Arrange
        val from = MDate.BuilderDk().dateTime(2020, 1,31).build()
        val to = MDate.BuilderDk().dateTime(2020, 2,1).build()

        // Act
        val diff = from.timeUntil(to, DateComponent.MONTH)

        // Assert
        Assert.assertEquals(1, diff)
    }

    @Test
    fun timeUntilDaysLong() {
        // Arrange
        val from = MDate.BuilderDk().dateTime(2020, 1,1).build()
        val to = MDate.BuilderDk().dateTime(2022, 1,1).build()

        // Act
        val diff = from.timeUntil(to, DateComponent.DAY)

        // Assert
        Assert.assertEquals(366 + 365, diff) //Days in 2020 + days in 2021
    }
    @Test
    fun timeUntilDaysShort() {
        // Arrange
        val from = MDate.BuilderDk().dateTime(day = 1, hour = 23, minute = 59, second = 59).build()
        val to = MDate.BuilderDk().dateTime(day = 2, hour = 1, minute = 0, second = 0).build()

        // Act
        val diff = from.timeUntil(to, DateComponent.DAY)

        // Assert
        Assert.assertEquals(1, diff)
    }

    @Test
    fun timeUntilHoursLong() {
        // Arrange
        val from = MDate.BuilderDk().dateTime(day = 1, hour = 5).build()
        val to = MDate.BuilderDk().dateTime(day = 3, hour = 3).build()

        // Act
        val diff = from.timeUntil(to, DateComponent.HOUR)

        // Assert
        Assert.assertEquals(46, diff)
    }
    @Test
    fun timeUntilHoursShort() {
        // Arrange
        val from = MDate.BuilderDk().dateTime(hour = 1, minute = 59, second = 59).build()
        val to = MDate.BuilderDk().dateTime(hour = 2, minute = 0, second = 0).build()

        // Act
        val diff = from.timeUntil(to, DateComponent.HOUR)

        // Assert
        Assert.assertEquals(1, diff)
    }

    @Test
    fun timeUntilMinutesLong() {
        // Arrange
        val from = MDate.BuilderDk().dateTime(day = 1, hour = 5, minute = 10).build()
        val to = MDate.BuilderDk().dateTime(day = 3, hour = 3, minute = 5).build()

        // Act
        val diff = from.timeUntil(to, DateComponent.MINUTE)

        // Assert
        Assert.assertEquals((46 * 60) - 5, diff)
    }
    @Test
    fun timeUntilMinutesShort() {
        // Arrange
        val from = MDate.BuilderDk().dateTime(minute = 0, second = 59).build()
        val to = MDate.BuilderDk().dateTime(minute = 1, second = 0).build()

        // Act
        val diff = from.timeUntil(to, DateComponent.MINUTE)

        // Assert
        Assert.assertEquals(1, diff)
    }

    @Test
    fun timeUntilSecondsLong() {
        // Arrange
        val from = MDate.BuilderDk().dateTime(minute = 10, second = 0).build()
        val to = MDate.BuilderDk().dateTime(minute = 55, second = 5).build()

        // Act
        val diff = from.timeUntil(to, DateComponent.SECOND)

        // Assert
        Assert.assertEquals((45 * 60) + 5, diff)
    }

    @Test
    fun timeUntilSecondsShort() {
        // Arrange
        val from = MDate.BuilderDk().dateTime(minute = 0, second = 0).build()
        val to = MDate.BuilderDk().dateTime(minute = 0, second = 1).build()

        // Act
        val diff = from.timeUntil(to, DateComponent.SECOND)

        // Assert
        Assert.assertEquals(1, diff)
    }

    @Test
    fun timeUntilNegativeYear() {
        // Arrange
        val from = MDate.BuilderDk().dateTime(2022).build()
        val to = MDate.BuilderDk().dateTime(2020).build()

        // Act
        val diff = from.timeUntil(to, DateComponent.YEAR)

        // Assert
        Assert.assertEquals(-2, diff)
    }

    @Test
    fun timeUntilNegativeHour() {
        // Arrange
        val from = MDate.BuilderDk().dateTime(hour = 23).build()
        val to = MDate.BuilderDk().dateTime(hour = 16).build()

        // Act
        val diff = from.timeUntil(to, DateComponent.HOUR)

        // Assert
        Assert.assertEquals(-7, diff)
    }
}