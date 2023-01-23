package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.MDate
import dk.mustache.corelib.utils.MDateBuilder
import dk.mustache.corelib.utils.MDateFormat
import org.junit.After
import org.junit.Assert
import org.junit.Test
import java.util.*

class MDateFormatTimeZoneTest {

    @After
    fun doAfter() {
        MDate.setDefaultTimeZone(TimeZone.getDefault())
    }

    @Test
    fun specifyTimeZoneOnMDateFormat() {
        // Arrange
        MDate.setDefaultTimeZone(TimeZone.getTimeZone("PRC"))

        val date = MDateBuilder(timeZone = TimeZone.getTimeZone("UTC"))
            .dateTime(month = 1, hour = 12, minute = 55)
            .build() // Month to 1 to avoid daylight savings
        val expectedOutput = "20:55"

        // Act
        val newDate = date.show(MDateFormat.TIME, localTimeZone = true) // From DefaultTimeZone, our local timezone is "PRC"

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun specifyTimeZoneOnCustomFormat() {
        // Arrange
        MDate.setDefaultTimeZone(TimeZone.getTimeZone("PRC")) // China Standard Time (+8)

        val date = MDateBuilder(timeZone = TimeZone.getTimeZone("UTC"))
            .dateTime(month = 1, hour = 12, minute = 55).build() // Month to 1 to avoid daylight savings
        val pattern = "HH:mm"
        val expectedOutput = "20:55"

        // Act
        val newDate = date.show(pattern, localTimeZone = true) // From DefaultTimeZone, our local timezone is "PRC"

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }
}