package dk.mustache.corelib.mdate

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    MDateBuilderTest::class,
    MDateCalcTest::class,
    MDateOtherTest::class,
    MDatePropertiesTest::class,
)
class MDateTest {
}