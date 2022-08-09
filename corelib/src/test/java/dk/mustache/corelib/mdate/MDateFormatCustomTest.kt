package dk.mustache.corelib.mdate

import dk.mustache.corelib.utils.CaseType
import dk.mustache.corelib.utils.MDate
import dk.mustache.corelib.utils.MDateFormat.*
import org.junit.Assert
import org.junit.Test

class MDateFormatCustomTest {
    @Test
    fun formatDateYear() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 21).build()
        val pattern = "dd/MM YYYY"
        val expectedOutput = "21/06 2000"

        // Act
        val newDate = date.show(pattern)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatTimeStamp() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(year = 2000, month = 6, day = 21, hour = 12, minute = 55, second = 47).build()
        val pattern = "YY/MM/dd-HH:mm:ss"
        val expectedOutput = "00/06/21-12:55:47"

        // Act
        val newDate = date.show(pattern)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatUppercase() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(month = 6, day = 21).build()
        val pattern = "dd. MMMM"
        val expectedOutput = "21. JUNI"

        // Act
        val newDate = date.show(pattern, CaseType.UpperCase)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatMapper() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(month = 12, day = 24).build()
        val pattern = "dd/MM"
        val mapper = {date: String -> if(date == "24/12") "Christmas" else date}
        val expectedOutput = "Christmas"

        // Act
        val newDate = date.show(pattern, mapper = mapper)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatMapper2() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(month = 11, day = 24).build()
        val pattern = "dd/MM"
        val mapper = {date: String -> if(date == "24/12") "Christmas" else date}
        val expectedOutput = "24/11"

        // Act
        val newDate = date.show(pattern, mapper = mapper)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }

    @Test
    fun formatMapperCaseCombo() {
        // Arrange
        val date = MDate.BuilderDk().dateTime(month = 12, day = 24).build()
        val pattern = "dd/MM"
        val mapper = {date: String -> if(date == "24/12") "Christmas" else date}
        val expectedOutput = "Christmas"
        // Casing is done before mapping, so this is same as formatMapper test

        // Act
        val newDate = date.show(pattern, CaseType.UpperCase, mapper = mapper)

        // Assert
        Assert.assertEquals(expectedOutput, newDate)
    }
}