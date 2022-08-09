package dk.mustache.corelibexample.mdate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dk.mustache.corelib.utils.CaseType
import dk.mustache.corelib.utils.DateComponent
import dk.mustache.corelib.utils.MDate
import dk.mustache.corelib.utils.MDateFormat.*
import dk.mustache.corelibexample.databinding.FragmentMdateBinding
import java.util.*

class MdateFragment : Fragment() {
    lateinit var binding: FragmentMdateBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMdateBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDateManipulation()
        setupTimeManipulation()
        setupShowDates()
        setupOther()
        setupConvenienceMethods()
    }

    private fun setupDateManipulation() {
        val date = createRandomMdate()
        binding.txtDemoDate.text = "+2 months, +15 days:\n" +
                date.show(DATE_YEAR_REVERSE) + " to " +
                date.plusDate(months = 2, days = 15).show(DATE_YEAR_REVERSE)

        val date2 = createRandomMdate()
        binding.txtDemoYear.text = "-3 years:\n" +
                date2.show(DATE_YEAR_REVERSE) + " to " +
                date2.plusDate(years = -3).show(DATE_YEAR_REVERSE)

        val date3 = createRandomMdate()
        binding.txtDemoMonth.text = "+11 months:\n" +
                date3.show(DATE_YEAR_REVERSE) + " to " +
                date3.plusMonths(11).show(DATE_YEAR_REVERSE)

        val date4 = createRandomMdate()
        binding.txtDemoDay.text = "-20 days:\n" +
                date4.show(DATE_YEAR) + " to " +
                date4.plusDays(-20).show(DATE_YEAR)
    }

    private fun setupTimeManipulation() {
        val date5 = createRandomMdate()
        binding.txtDemoTime.text = "+6 hours, +30 minutes:\n" +
                date5.show(TIME) + " to " +
                date5.plusTime(hours = 6, minutes = 30).show(TIME)

        val date6 = createRandomMdate()
        val date6b = date6.plusHours(21)
        binding.txtDemoHour.text = "+21 hours:\n" +
                date6.show(TIMESTAMP) + " to " +
                date6b.show(TIMESTAMP)

        val date7 = createRandomMdate()
        binding.txtDemoMinute.text = "-55 minutes:\n" +
                date7.show(TIME) + " to " +
                date7.plusMinutes(-55).show(TIME)

        val date8 = createRandomMdate()
        binding.txtDemoSecond.text = "+30 seconds:\n" +
                date8.show(TIME_SECONDS) + " to " +
                date8.plusSeconds(30).show(TIME_SECONDS)
    }

    private fun setupShowDates() {
        val date = createRandomMdate()
        binding.txtDemoShowDateYear.text = "DATE_YEAR:\n" + date.show(DATE_YEAR)

        binding.txtDemoShowDateYearShort.text =
            "DATE_YEAR_SHORT:\n" + date.show(DATE_YEAR_SHORT)

        binding.txtShowDate.text = "DATE:\n" + date.show(DATE)

        binding.txtDemoShowPrettyDateYearLong.text =
            "PRETTYDATE_YEAR_LONG:\n" + date.show(PRETTYDATE_YEAR_LONG)

        binding.txtDemoShowPrettyDateYearShort.text =
            "PRETTYDATE_YEAR_SHORT:\n" + date.show(PRETTYDATE_YEAR_SHORT)

        binding.txtDemoShowPrettyDateYearVeryShort.text =
            "PRETTYDATE_YEAR_VERYSHORT (Uppercase):\n" +
                    date.show(PRETTYDATE_YEAR_VERYSHORT, CaseType.UpperCase)

        binding.txtDemoShowPrettyDateLong.text =
            "PRETTYDATE_LONG:\n" + date.show(PRETTYDATE_LONG)

        binding.txtDemoShowPrettyDateShort.text =
            "PRETTYDATE_SHORT:\n" + date.show(PRETTYDATE_SHORT)

        binding.txtDemoShowPrettyDateVeryShort.text =
            "PRETTYDATE_VERYSHORT (CapWords):\n" +
                    date.show(PRETTYDATE_VERYSHORT, CaseType.CapWords)

        binding.txtDemoShowWeekDayDate.text =
            "WEEKDAY_SHORT_DATE_LONG:\n" + date.show(WEEKDAY_SHORT_DATE_LONG)

        binding.txtDemoShowWeekDayLongDateYear.text =
            "PRETTY_WEEKDAY_LONG_DATE_D_LONG_YEAR:\n" + date.show(WEEKDAY_LONG_DATE_D_LONG_YEAR)
    }

    private fun setupOther() {
        val isoWeek1 = MDate.BuilderDk().date(year = 2020, month = 1, day = 1).build()
        binding.txtDemoShowIsoWeek1.text =
            "ISOWEEK_TWODIGIT(" + isoWeek1.show(DATE_YEAR) + "):\n" +
                    isoWeek1.show(ISOWEEK_TWODIGIT)

        val isoWeek2 = MDate.BuilderDk().date(year = 2020, month = 1, day = 1).build()
        binding.txtDemoShowIsoWeek2.text =
            "ISOWEEK_NATURAL (" + isoWeek2.show(DATE_YEAR) + "):\n" +
                    isoWeek2.show(ISOWEEK_NATURAL)

        val isoWeek3 = MDate.BuilderDk().date(year = 2022, month = 1, day = 1).build()
        binding.txtDemoShowIsoWeek3.text =
            "ISOWEEK_NATURAL (" + isoWeek3.show(DATE_YEAR) + " / edgecase; " +
                    "first couple of days belong to week-year 2021):\n" +
                    isoWeek3.show(ISOWEEK_NATURAL)

        val dateda = MDate.BuilderDk().date(year = 2022, month = 3, day = 1).build()
        binding.txtDemoDanish.text =
            "Danish Localization:\n" + dateda.show(PRETTYDATE_LONG)

        val datede = MDate.BuilderDe().date(year = 2022, month = 3, day = 1).build()
        binding.txtDemoGerman.text =
            "German Localization:\n" + datede.show(PRETTYDATE_LONG)

        val dateuk = MDate.BuilderGb().date(year = 2022, month = 3, day = 1).build()
        binding.txtDemoEnglish.text =
            "English/UK Localization:\n" + dateuk.show(PRETTYDATE_LONG)
    }

    private fun setupConvenienceMethods() {
        val propdate = createRandomMdate()
        binding.txtProperties.text = propdate.show(DATE_YEAR) + " " + propdate.show(
            TIME_SECONDS
        ) +
                ": Year: ${propdate.year} Mon: ${propdate.month} Day: ${propdate.day} " +
                "WeekDay: ${propdate.dayOfWeek} " +
                "Hour: ${propdate.hour} Min: ${propdate.minute} Sec: ${propdate.second}"


        val dateEqual = createRandomMdate()
        val dateEqual2 = dateEqual.clone(hour = 12, minute = 36, second = 12)
        binding.txtEqualDayTrue.text = dateEqual.show(TIMESTAMP) + " is on same day as " +
                dateEqual2.show(TIMESTAMP) + ": " + dateEqual.equals(dateEqual2, DateComponent.DAY)

        val dateEqual3 = dateEqual.plusDays(1)
        binding.txtEqualDayFalse.text = dateEqual.show(TIMESTAMP) + " is on same day as " +
                dateEqual3.show(TIMESTAMP) + ": " + dateEqual.equals(dateEqual3, DateComponent.DAY)

        val dateFrom = createRandomMdate()
        val dateto = createRandomMdate()
        binding.txtTimeUntil.text = dateFrom.show(DATE_YEAR) + " months until " + dateto.show(DATE_YEAR) +
                ": " + dateFrom.timeUntil(dateto, DateComponent.MONTH)

        val dateToRound = createRandomMdate()
        val roundedDate = dateToRound.roundToDate()
        binding.txtRoundToDate.text =
            dateToRound.show(DATE_YEAR) + " " + dateToRound.show(TIME_SECONDS) +
                    " converts to " + roundedDate.show(DATE_YEAR) + " " + roundedDate.show(
                TIME_SECONDS
            )

        val compareDate1 = createRandomMdate()
        val compareDate2 = createRandomMdate()
        binding.txtCompareTo.text =
            compareDate1.show(TIMESTAMP) +
                    " <= " + compareDate2.show(TIMESTAMP) + " = " +
                    (compareDate1 <= compareDate2)
    }

    private fun createRandomMdate(): MDate {
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