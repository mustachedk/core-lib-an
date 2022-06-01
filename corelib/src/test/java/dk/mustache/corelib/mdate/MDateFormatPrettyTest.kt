package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.MDate
import dk.mustache.corelib.utils.MDateFormat
import org.junit.Assert
import org.junit.Test

class MDateFormatPrettyTest {
    @Test
    fun formatPrettyDateLongJanuary() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 1, day = 21).build()
        val expectedOutput = "21. januar"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateLongFebruary() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 2, day = 21).build()
        val expectedOutput = "21. februar"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateLongMarch() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 3, day = 21).build()
        val expectedOutput = "21. marts"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateLongApril() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 4, day = 21).build()
        val expectedOutput = "21. april"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateLongMay() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 5, day = 21).build()
        val expectedOutput = "21. maj"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateLongJune() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 21).build()
        val expectedOutput = "21. juni"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateLongJuly() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 7, day = 21).build()
        val expectedOutput = "21. juli"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateLongAugust() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 8, day = 21).build()
        val expectedOutput = "21. august"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateLongSeptember() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 9, day = 21).build()
        val expectedOutput = "21. september"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateLongOctober() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 10, day = 21).build()
        val expectedOutput = "21. oktober"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateLongNovember() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 11, day = 21).build()
        val expectedOutput = "21. november"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateLongDecember() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 12, day = 21).build()
        val expectedOutput = "21. december"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateShortJanuary() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 1, day = 21).build()
        val expectedOutput = "21. jan."

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_SHORT)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateShortFebruary() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 2, day = 21).build()
        val expectedOutput = "21. feb."

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_SHORT)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateShortMarch() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 3, day = 21).build()
        val expectedOutput = "21. mar."

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_SHORT)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateShortApril() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 4, day = 21).build()
        val expectedOutput = "21. apr."

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_SHORT)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateShortMay() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 5, day = 21).build()
        val expectedOutput = "21. maj"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_SHORT)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateShortJune() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 21).build()
        val expectedOutput = "21. jun."

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_SHORT)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateShortJuly() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 7, day = 21).build()
        val expectedOutput = "21. jul."

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_SHORT)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateShortAugust() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 8, day = 21).build()
        val expectedOutput = "21. aug."

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_SHORT)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateShortSeptember() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 9, day = 21).build()
        val expectedOutput = "21. sep."

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_SHORT)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateShortOctober() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 10, day = 21).build()
        val expectedOutput = "21. okt."

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_SHORT)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateShortNovember() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 11, day = 21).build()
        val expectedOutput = "21. nov."

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_SHORT)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateShortDecember() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 12, day = 21).build()
        val expectedOutput = "21. dec."

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_SHORT)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateVeryShort() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 1, day = 21).build()
        val expectedOutput = "21.jan."

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_VERYSHORT)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateYearLong() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 1, day = 21).build()
        val expectedOutput = "21. januar 2000"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_YEAR_LONG)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateYearShort() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 1, day = 21).build()
        val expectedOutput = "21. jan. 2000"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_YEAR_SHORT)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatPrettyDateYearVeryShort() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2013, month = 1, day = 21).build()
        val expectedOutput = "21.jan. 13"

        // Act
        val newDate = date.show(MDateFormat.PRETTYDATE_YEAR_VERYSHORT)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }
}