package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.MDate
import dk.mustache.corelib.utils.MDateBuilder
import dk.mustache.corelib.utils.MDateFormat
import org.junit.After
import org.junit.Assert
import org.junit.Test
import java.sql.Time
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

        val date = MDateBuilder(defaultBuilderTimeZone = TimeZone.getTimeZone("UTC"))
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

        val date = MDateBuilder(defaultBuilderTimeZone = TimeZone.getTimeZone("UTC"))
            .dateTime(month = 1, hour = 12, minute = 55).build() // Month to 1 to avoid daylight savings
        val pattern = "HH:mm"
        val expectedOutput = "20:55"

        // Act
        val newDate = date.show(pattern, localTimeZone = true) // From DefaultTimeZone, our local timezone is "PRC"

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun timeZoneParseToDefault() {
        // Arrange
        MDate.setDefaultTimeZone(TimeZone.getTimeZone("UTC"))

        val timeZone = TimeZone.getTimeZone("PRC") // China Standard Time (+8)
        val dateString = "2022-01-01 12:00" // Time in PRC
        val pattern = "yyyy-MM-dd HH:mm" // January 1st to avoid daylight savings
        val expectedHour = 4 // Time in UTC (Our default)

        // Act
        val date = MDate.Builder().parse(dateString, pattern, parsetimeZone = timeZone)

        // Assert
        Assert.assertEquals(expectedHour, date.hour)
    }

    @Test
    fun timeZoneParseToDefaultSummerTime() {
        // Arrange
        MDate.setDefaultTimeZone(TimeZone.getTimeZone("UTC"))

        val timeZone = TimeZone.getTimeZone("Europe/Kiev") // (+2) / (+3 during daylight savings)
        val dateString = "2022-06-01 12:00" // Time in Kiev
        val pattern = "yyyy-MM-dd HH:mm" // June 6th to trigger daylight savings
        val expectedHour = 9 // Time in UTC (Our default)

        // Act
        val date = MDate.Builder().parse(dateString, pattern, parsetimeZone = timeZone)

        // Assert
        Assert.assertEquals(expectedHour, date.hour)
    }

    @Test
    fun timeZoneParseToSpecificTimeZone() {
        // Arrange
        MDate.setDefaultTimeZone(TimeZone.getTimeZone("UTC")) // (+0)

        val timeZoneOutput = TimeZone.getTimeZone("Europe/Kiev") // (+2)
        val timeZoneInput = TimeZone.getTimeZone("PRC") // China Standard Time (+8)
        val dateString = "2022-01-01 12:00" // Time in PRC
        val pattern = "yyyy-MM-dd HH:mm" // January 1st to avoid daylight savings
        val expectedHour = 6 // 12 - 8 + 2 = Time in Kiev

        // Act
        val date = MDateBuilder(defaultBuilderTimeZone = timeZoneOutput).parse(dateString, pattern, parsetimeZone = timeZoneInput)

        // Assert
        Assert.assertEquals(expectedHour, date.hour)
    }
}