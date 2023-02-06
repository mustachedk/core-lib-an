package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.MDate
import dk.mustache.corelib.utils.MDateBuilder
import dk.mustache.corelib.utils.MDateFormat
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
import org.junit.After
import org.junit.Assert
import org.junit.Test
import java.sql.Time
import java.util.*

class MDateTimeZoneDefaultTest {

    @After
    fun doAfter() {
        MDate.setDefaultTimeZone(TimeZone.getDefault())
    }

    //
    // Time should not get changed, if we have not specified a timezone change, even if out default
    // timezone is not the same as system time. If a defaultTimeZone has been set, that is the
    // assumed timezone for all actions.
    //

    @Test
    fun show() {
        // Arrange
        MDate.setDefaultTimeZone(TimeZone.getTimeZone("PRC")) // China Standard Time (+8)

        val date = MDate.Builder().dateTime(hour = 12, minute = 55).build()
        val expectedOutput = "12:55"

        // Act
        val newDate = date.show(MDateFormat.TIME, useDefaultTimeZone = true)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun parse() {
        // Arrange
        MDate.setDefaultTimeZone(TimeZone.getTimeZone("PRC")) // China Standard Time (+8)

        val dateString = "2022-01-01 12:00"
        val pattern = "yyyy-MM-dd HH:mm"
        val expectedHour = 12

        // Act
        val date = MDate.Builder().parse(dateString, pattern)

        // Assert
        Assert.assertEquals(expectedHour, date.hour)
    }

    @Test
    fun now() {
        // Arrange
        MDate.setDefaultTimeZone(TimeZone.getTimeZone("PRC")) // China Standard Time (+8)

        val jodaNow = LocalDateTime.now().toDateTime(DateTimeZone.UTC)
        val expectedHourNow = jodaNow.hourOfDay + 8

        // Act
        val date = MDate.Builder().now()

        // Assert
        Assert.assertEquals(expectedHourNow, date.hour)
    }
}