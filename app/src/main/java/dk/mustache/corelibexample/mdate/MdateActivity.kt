package dk.mustache.corelibexample.mdate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import dk.mustache.corelib.utils.CaseType
import dk.mustache.corelib.utils.MDate
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
        txtDate.text = "Date Manipulation: +2 months, +15 days:\n" +
                date.showYearMonthDay() + " to " +
                date.plusDate(months = 2, days = 15).showYearMonthDay()

        val txtYear = findViewById<TextView>(R.id.txtDemoYear)
        val date2 = createMdate()
        txtYear.text = "Date Manipulation: -3 years:\n" +
                date2.showYearMonthDay() + " to " +
                date2.plusDate(years = -3).showYearMonthDay()

        val txtMonths = findViewById<TextView>(R.id.txtDemoMonth)
        val date3 = createMdate()
        txtMonths.text = "Date Manipulation: +11 months:\n" +
                date3.showYearMonthDay() + " to " +
                date3.plusMonths(11).showYearMonthDay()

        val txtDays = findViewById<TextView>(R.id.txtDemoDay)
        val date4 = createMdate()
        txtDays.text = "Date Manipulation: -20 days:\n" +
                date4.showDayMonthYear() + " to " +
                date4.plusDays(-20).showDayMonthYear()
    }

    private fun setupTimeManipulation() {
        val txtTime = findViewById<TextView>(R.id.txtDemoTime)
        val date5 = createMdate()
        txtTime.text = "Date Manipulation: +6 hours, +30 minutes:\n" +
                date5.showHourMinute() + " to " +
                date5.plusTime(hours = 6, minutes = 30).showHourMinute()

        val txtHour = findViewById<TextView>(R.id.txtDemoHour)
        val date6 = createMdate()
        val date6b = date6.plusHours(21)
        txtHour.text = "Date Manipulation: +21 hours:\n" +
                date6.showDayMonth() + " - " + date6.showHourMinute() + " to " +
                date6b.showDayMonth() + " - " + date6b.showHourMinute()

        val txtMinute = findViewById<TextView>(R.id.txtDemoMinute)
        val date7 = createMdate()
        txtMinute.text = "Date Manipulation: -55 minutes:\n" +
                date7.showHourMinute() + " to " +
                date7.plusMinutes(-55).showHourMinute()

        val txtSecond = findViewById<TextView>(R.id.txtDemoSecond)
        val date8 = createMdate()
        txtSecond.text = "Date Manipulation: +30 seconds:\n" +
                date8.showTime() + " to " +
                date8.plusSeconds(30).showTime()
    }

    private fun setupShowDates() {
        val date = createMdate()
        val txtDemoDayMonthYear = findViewById<TextView>(R.id.txtDemoShowDayMonthYear)
        txtDemoDayMonthYear.text =
            "ShowDayMonthYear:\n" + date.showDayMonthYear()

        val txtDemoShortDayMonthYear = findViewById<TextView>(R.id.txtDemoShowShortDayMonthYear)
        txtDemoShortDayMonthYear.text =
            "ShowShortDayMonthYear:\n" + date.showShortDayMonthYear()

        val txtDemoDayMonth = findViewById<TextView>(R.id.txtDemoShowDayMonth)
        txtDemoDayMonth.text =
            "ShowDayMonth:\n" + date.showDayMonth()

        val txtDemoLongPrettyDateYear = findViewById<TextView>(R.id.txtDemoLongPrettyDateYear)
        txtDemoLongPrettyDateYear.text =
            "LongPrettyDateYear:\n" + date.showLongPrettyDateYear()

        val txtDemoShortPrettyDateYear = findViewById<TextView>(R.id.txtDemoShortPrettyDateYear)
        txtDemoShortPrettyDateYear.text =
            "ShortPrettyDateYear:\n" + date.showShortPrettyDateYear()

        val txtDemoVeryShortPrettyDateYear =
            findViewById<TextView>(R.id.txtDemoVeryShortPrettyDateYear)
        txtDemoVeryShortPrettyDateYear.text =
            "VeryShortPrettyDateYear (Uppercase):\n" +
                    date.showVeryShortPrettyDateYear(CaseType.UpperCase)

        val txtDemoLongPrettyDate = findViewById<TextView>(R.id.txtDemoLongPrettyDate)
        txtDemoLongPrettyDate.text =
            "LongPrettyDate:\n" + date.showLongPrettyDate()

        val txtDemoShortPrettyDate = findViewById<TextView>(R.id.txtDemoShortPrettyDate)
        txtDemoShortPrettyDate.text =
            "ShortPrettyDate:\n" + date.showShortPrettyDate()

        val txtDemoVeryShortPrettyDate = findViewById<TextView>(R.id.txtDemoVeryShortPrettyDate)
        txtDemoVeryShortPrettyDate.text =
            "VeryShortPrettyDate (Uppercase):\n" +
                    date.showVeryShortPrettyDate(CaseType.UpperCase)
    }

    private fun setupOther() {
        val isoWeek1 = MDate.BuilderDk().date(year=2020 , month=1 , day=1).build()
        val txtDemoShowIsoWeek1 = findViewById<TextView>(R.id.txtDemoShowIsoWeek1)
        txtDemoShowIsoWeek1.text =
            "ShowIsoWeek (padded):\n" +
                    isoWeek1.showDayMonthYear() + " Week: " + isoWeek1.showIsoWeek(true)

        val isoWeek2 = MDate.BuilderDk().date(year=2022 , month=1, day=2).build()
        val txtDemoShowIsoWeek2 = findViewById<TextView>(R.id.txtDemoShowIsoWeek2)
        txtDemoShowIsoWeek2.text =
            "ShowIsoWeek (edgecase; first couple of days belong to week-year 2021):\n" +
                    isoWeek2.showDayMonthYear() + " Week: " + isoWeek2.showIsoWeek()

        val dateda = MDate.BuilderDk().date(year=2022 , month=3, day=1).build()
        val txtDemoDanish = findViewById<TextView>(R.id.txtDemoDanish)
        txtDemoDanish.text = "Danish Localization:\n" + dateda.showLongPrettyDate()

        val datede = MDate.BuilderDe().date(year=2022 , month=3, day=1).build()
        val txtDemoGerman = findViewById<TextView>(R.id.txtDemoGerman)
        txtDemoGerman.text = "German Localization:\n" + datede.showLongPrettyDate()

        val dateuk = MDate.BuilderGb().date(year=2022 , month=3, day=1).build()
        val txtDemoEnglish = findViewById<TextView>(R.id.txtDemoEnglish)
        txtDemoEnglish.text = "English/UK Localization:\n" + dateuk.showLongPrettyDate()
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