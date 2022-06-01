package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.MDate
import dk.mustache.corelib.utils.MDateBuilder
import org.junit.Assert
import org.junit.Test

class MDatePropertiesTest {

    @Test
    fun getYear1962() {
        // Arrange
        val testYear = 1962
        val date = getDateTimeBuilder().year(testYear).build()

        // Act
        val actualYear = date.year

        // Assert
        Assert.assertEquals(testYear, actualYear)
    }

    @Test
    fun getYear2042() {
        // Arrange
        val testYear = 2042
        val date = getDateTimeBuilder().year(testYear).build()

        // Act
        val actualYear = date.year

        // Assert
        Assert.assertEquals(testYear, actualYear)
    }

    @Test
    fun getMonth() {
        // Arrange
        val testMonth = 6
        val date = getDateTimeBuilder().month(testMonth).build()

        // Act
        val actualMonth = date.month

        // Assert
        Assert.assertEquals(testMonth, actualMonth)
    }

    @Test
    fun getDay() {
        // Arrange
        val testDay = 6
        val date = getDateTimeBuilder().day(testDay).build()

        // Act
        val actualDay = date.day

        // Assert
        Assert.assertEquals(testDay, actualDay)
    }

    @Test
    fun getHour() {
        // Arrange
        val testHour = 6
        val date = getDateTimeBuilder().hour(testHour).build()

        // Act
        val actualHour = date.hour

        // Assert
        Assert.assertEquals(testHour, actualHour)
    }

    @Test
    fun getMinute() {
        // Arrange
        val testMinute = 6
        val date = getDateTimeBuilder().minute(testMinute).build()

        // Act
        val actualMinute = date.minute

        // Assert
        Assert.assertEquals(testMinute, actualMinute)
    }

    @Test
    fun getSecond() {
        // Arrange
        val testSecond = 6
        val date = getDateTimeBuilder().second(testSecond).build()

        // Act
        val actualSecond = date.second

        // Assert
        Assert.assertEquals(testSecond, actualSecond)
    }

    private fun getDateTimeBuilder(): MDateBuilder {
        return MDate.BuilderDk().dateTime(2000,1,1,0,0,0)
    }
}