package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.MDate
import org.junit.Assert
import org.junit.Test

class MDateComparisonIsNowTest {
    @Test
    fun isNowDefault() {
        // Arrange
        val now = MDate.BuilderDk().now()
        val date = now.plusHours(4)

        // Act
        val isNow = date.isNow()

        // Assert
        Assert.assertTrue(isNow)
    }
    @Test
    fun isNowNotDefault() {
        // Arrange
        val now = MDate.BuilderDk().now()
        val date = now.plusDays(1)

        // Act
        val isNow = date.isNow()

        // Assert
        Assert.assertFalse(isNow)
    }

    @Test
    fun isToday() {
        // Arrange
        val now = MDate.BuilderDk().now()
        val date = now.plusHours(4)

        // Act
        val isToday = date.isToday()

        // Assert
        Assert.assertTrue(isToday)
    }
    @Test
    fun isTodayNot() {
        // Arrange
        val now = MDate.BuilderDk().now()
        val date = now.plusDays(1)

        // Act
        val isToday = date.isToday()

        // Assert
        Assert.assertFalse(isToday)
    }

    @Test
    fun isYesterDay() {
        // Arrange
        val now = MDate.BuilderDk().now()
        val date = now.plusDays(-1)

        // Act
        val isYesterDay = date.isYesterday()

        // Assert
        Assert.assertTrue(isYesterDay)
    }
    @Test
    fun isYesterDayNot() {
        // Arrange
        val now = MDate.BuilderDk().now()

        // Act
        val isYesterDay = now.isYesterday()

        // Assert
        Assert.assertFalse(isYesterDay)
    }

    @Test
    fun isTomorrow() {
        // Arrange
        val now = MDate.BuilderDk().now()
        val date = now.plusDays(+1)

        // Act
        val isTomorrow = date.isTomorrow()

        // Assert
        Assert.assertTrue(isTomorrow)
    }
    @Test
    fun isTomorrowNot() {
        // Arrange
        val now = MDate.BuilderDk().now()

        // Act
        val isTomorrow = now.isTomorrow()

        // Assert
        Assert.assertFalse(isTomorrow)
    }
}