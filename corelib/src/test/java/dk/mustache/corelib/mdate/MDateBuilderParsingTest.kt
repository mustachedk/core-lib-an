package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.MDate
import org.junit.Assert
import org.junit.Test
import org.junitpioneer.jupiter.DefaultTimeZone
import java.util.*

class MDateBuilderParsingTest {

    @Test
    fun parseJavaDate() {
        // Arrange
        val javaDate = Date(1532516399000)
        val expectedYear = 2018
        val expectedMonth = 7
        val expectedDay = 25
        val expectedHour = 10
        val expectedMinute = 59
        val expectedSecond = 59

        // Act
        val date = MDate.BuilderDk(timeZone = TimeZone.getTimeZone("UTC")).fromJavaDate(javaDate)

        // Assert
        Assert.assertEquals(expectedYear, date.year)
        Assert.assertEquals(expectedMonth, date.month)
        Assert.assertEquals(expectedDay, date.day)
        Assert.assertEquals(expectedHour, date.hour)
        Assert.assertEquals(expectedMinute, date.minute)
        Assert.assertEquals(expectedSecond, date.second)
    }

    @Test
    fun parseStringWithPattern() {
        // Arrange
        val pattern = "yyyy-MM-dd'T'HH:mm:ss"
        val string = "2018-07-25T10:59:59"
        val expectedYear = 2018
        val expectedMonth = 7
        val expectedDay = 25
        val expectedHour = 10
        val expectedMinute = 59
        val expectedSecond = 59

        // Act
        val date = MDate.BuilderDk().parse(string, pattern)

        // Assert
        Assert.assertEquals(expectedYear, date.year)
        Assert.assertEquals(expectedMonth, date.month)
        Assert.assertEquals(expectedDay, date.day)
        Assert.assertEquals(expectedHour, date.hour)
        Assert.assertEquals(expectedMinute, date.minute)
        Assert.assertEquals(expectedSecond, date.second)
    }
}