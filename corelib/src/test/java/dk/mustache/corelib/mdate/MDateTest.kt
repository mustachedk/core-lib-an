package dk.mustache.corelib.mdate

import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 *
 * Run this suite to run all MDate tests
 *
 */

@RunWith(Suite::class)
@Suite.SuiteClasses(
    MDateBuilderTest::class,
    MDateBuilderParsingTest::class,
    MDateCalcTest::class,
    MDateCloneTest::class,
    MDateComparisonBasicTest::class,
    MDateComparisonEqualsComponentTest::class,
    MDateComparisonIsNowTest::class,
    MDateComparisonUntilTest::class,
    MDateFormatCustomTest::class,
    MDateFormatIsoWeekTest::class,
    MDateFormatNoPeriodTest::class,
    MDateFormatPrettyTest::class,
    MDateFormatSimpleTest::class,
    MDateTimeZoneChangeTest::class,
    MDateTimeZoneDefaultTest::class,
    MDateFormatWeekDayTest::class,
    MDatePropertiesTest::class,
    MDateRoundingTest::class,
)
class MDateTest {
}