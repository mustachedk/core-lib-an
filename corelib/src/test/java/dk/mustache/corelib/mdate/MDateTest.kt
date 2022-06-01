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
    MDateCalcTest::class,
    MDateFormatIsoWeekTest::class,
    MDateFormatPrettyTest::class,
    MDateFormatSimpleTest::class,
    MDateOtherTest::class,
    MDatePropertiesTest::class,
)
class MDateTest {
}