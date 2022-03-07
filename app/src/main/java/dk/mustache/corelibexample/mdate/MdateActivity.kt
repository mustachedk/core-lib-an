package dk.mustache.corelibexample.mdate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import dk.mustache.corelib.utils.CaseType
import dk.mustache.corelib.utils.MDate
import dk.mustache.corelib.utils.MDateFormat.*
import dk.mustache.corelibexample.R
import java.util.*

class MdateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mdate)
    }

    override fun onResume() {
        super.onResume()

        setupDateManipulation()
        setupTimeManipulation()
        setupShowDates()
        setupOther()
    }

    private fun setupDateManipulation() {
        val txtDate = findViewById<TextView>(R.id.txtDemoDate)
        val date = createMdate()
        txtDate.text = "+2 months, +15 days:\n" +
                date.show(DATE_YEAR_REVERSE) + " to " +
                date.plusDate(months = 2, days = 15).show(DATE_YEAR_REVERSE)

        val txtYear = findViewById<TextView>(R.id.txtDemoYear)
        val date2 = createMdate()
        txtYear.text = "-3 years:\n" +
                date2.show(DATE_YEAR_REVERSE) + " to " +
                date2.plusDate(years = -3).show(DATE_YEAR_REVERSE)

        val txtMonths = findViewById<TextView>(R.id.txtDemoMonth)
        val date3 = createMdate()
        txtMonths.text = "+11 months:\n" +
                date3.show(DATE_YEAR_REVERSE) + " to " +
                date3.plusMonths(11).show(DATE_YEAR_REVERSE)

        val txtDays = findViewById<TextView>(R.id.txtDemoDay)
        val date4 = createMdate()
        txtDays.text = "-20 days:\n" +
                date4.show(DATE_YEAR) + " to " +
                date4.plusDays(-20).show(DATE_YEAR)
    }

    private fun setupTimeManipulation() {
        val txtTime = findViewById<TextView>(R.id.txtDemoTime)
        val date5 = createMdate()
        txtTime.text = "+6 hours, +30 minutes:\n" +
                date5.show(TIME) + " to " +
                date5.plusTime(hours = 6, minutes = 30).show(TIME)

        val txtHour = findViewById<TextView>(R.id.txtDemoHour)
        val date6 = createMdate()
        val date6b = date6.plusHours(21)
        txtHour.text = "+21 hours:\n" +
                date6.show(DATE) + " - " + date6.show(TIME) + " to " +
                date6b.show(DATE) + " - " + date6b.show(TIME)

        val txtMinute = findViewById<TextView>(R.id.txtDemoMinute)
        val date7 = createMdate()
        txtMinute.text = "-55 minutes:\n" +
                date7.show(TIME) + " to " +
                date7.plusMinutes(-55).show(TIME)

        val txtSecond = findViewById<TextView>(R.id.txtDemoSecond)
        val date8 = createMdate()
        txtSecond.text = "+30 seconds:\n" +
                date8.show(TIME_SECONDS) + " to " +
                date8.plusSeconds(30).show(TIME_SECONDS)
    }

    private fun setupShowDates() {
        val date = createMdate()
        val txtDemoShowDateYear = findViewById<TextView>(R.id.txtDemoShowDateYear)
        txtDemoShowDateYear.text = "DATE_YEAR:\n" + date.show(DATE_YEAR)

        val txtDemoShowDateYearShort = findViewById<TextView>(R.id.txtDemoShowDateYearShort)
        txtDemoShowDateYearShort.text = "DATE_YEAR_SHORT:\n" + date.show(DATE_YEAR_SHORT)

        val txtShowDate = findViewById<TextView>(R.id.txtShowDate)
        txtShowDate.text = "DATE:\n" + date.show(DATE)

        val txtDemoShowPrettyDateYearLong =
            findViewById<TextView>(R.id.txtDemoShowPrettyDateYearLong)
        txtDemoShowPrettyDateYearLong.text =
            "PRETTYDATE_YEAR_LONG:\n" + date.show(PRETTYDATE_YEAR_LONG)

        val txtDemoShowPrettyDateYearShort =
            findViewById<TextView>(R.id.txtDemoShowPrettyDateYearShort)
        txtDemoShowPrettyDateYearShort.text =
            "PRETTYDATE_YEAR_SHORT:\n" + date.show(PRETTYDATE_YEAR_SHORT)

        val txtDemoShowPrettyDateYearVeryShort =
            findViewById<TextView>(R.id.txtDemoShowPrettyDateYearVeryShort)
        txtDemoShowPrettyDateYearVeryShort.text =
            "PRETTYDATE_YEAR_VERYSHORT (Uppercase):\n" +
                    date.show(PRETTYDATE_YEAR_VERYSHORT, CaseType.UpperCase)

        val txtDemoShowPrettyDateLong = findViewById<TextView>(R.id.txtDemoShowPrettyDateLong)
        txtDemoShowPrettyDateLong.text =
            "PRETTYDATE_LONG:\n" + date.show(PRETTYDATE_LONG)

        val txtDemoShowPrettyDateShort = findViewById<TextView>(R.id.txtDemoShowPrettyDateShort)
        txtDemoShowPrettyDateShort.text =
            "PRETTYDATE_SHORT:\n" + date.show(PRETTYDATE_SHORT)

        val txtDemoShowPrettyDateVeryShort =
            findViewById<TextView>(R.id.txtDemoShowPrettyDateVeryShort)
        txtDemoShowPrettyDateVeryShort.text =
            "PRETTYDATE_VERYSHORT (Uppercase):\n" +
                    date.show(PRETTYDATE_VERYSHORT, CaseType.UpperCase)
    }

    private fun setupOther() {
        val isoWeek1 = MDate.BuilderDk().date(year = 2020, month = 1, day = 1).build()
        val txtDemoShowIsoWeek1 = findViewById<TextView>(R.id.txtDemoShowIsoWeek1)
        txtDemoShowIsoWeek1.text =
            "ISOWEEK_TWODIGIT(" + isoWeek1.show(DATE_YEAR) + "):\n" +
                    isoWeek1.show(ISOWEEK_TWODIGIT)

        val isoWeek2 = MDate.BuilderDk().date(year = 2020, month = 1, day = 1).build()
        val txtDemoShowIsoWeek2 = findViewById<TextView>(R.id.txtDemoShowIsoWeek2)
        txtDemoShowIsoWeek2.text =
            "ISOWEEK_NATURAL (" + isoWeek2.show(DATE_YEAR) + "):\n" +
                    isoWeek2.show(ISOWEEK_NATURAL)

        val isoWeek3 = MDate.BuilderDk().date(year = 2022, month = 1, day = 1).build()
        val txtDemoShowIsoWeek3 = findViewById<TextView>(R.id.txtDemoShowIsoWeek3)
        txtDemoShowIsoWeek3.text =
            "ISOWEEK_NATURAL (" + isoWeek3.show(DATE_YEAR) + " / edgecase; " +
                    "first couple of days belong to week-year 2021):\n" +
                    isoWeek3.show(ISOWEEK_NATURAL)

        val dateda = MDate.BuilderDk().date(year = 2022, month = 3, day = 1).build()
        val txtDemoDanish = findViewById<TextView>(R.id.txtDemoDanish)
        txtDemoDanish.text = "Danish Localization:\n" + dateda.show(PRETTYDATE_LONG)

        val datede = MDate.BuilderDe().date(year = 2022, month = 3, day = 1).build()
        val txtDemoGerman = findViewById<TextView>(R.id.txtDemoGerman)
        txtDemoGerman.text = "German Localization:\n" + datede.show(PRETTYDATE_LONG)

        val dateuk = MDate.BuilderGb().date(year = 2022, month = 3, day = 1).build()
        val txtDemoEnglish = findViewById<TextView>(R.id.txtDemoEnglish)
        txtDemoEnglish.text = "English/UK Localization:\n" + dateuk.show(PRETTYDATE_LONG)
    }

    private fun createMdate(): MDate {
        val rand = Random()
        val year = 2000 + rand.nextInt(30)
        val month = 1 + rand.nextInt(11)
        val day = 1 + rand.nextInt(30)
        val hour = rand.nextInt(24)
        val minute = rand.nextInt(60)
        val second = rand.nextInt(60)
        return MDate.BuilderDk()
            .date(year, month, day)
            .time(hour, minute, second)
            .build()
    }
}