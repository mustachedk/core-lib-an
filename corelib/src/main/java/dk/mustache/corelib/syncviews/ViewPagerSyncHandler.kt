package dk.mustache.corelib.syncviews

import androidx.viewpager2.widget.ViewPager2
import kotlin.reflect.KFunction2

class ViewPagerSyncHandler(
    private val viewPager: ViewPager2,
    conductor: SyncConductor<() -> Int>,
    private val smoothScroll: Boolean = true
) : PositionSyncSubject {

    override var sendEvent: KFunction2<SyncSubject<() -> Int>, () -> Int, Unit>? = null
    override var eventIsOnGoing: Boolean = false

    init {
        conductor.subscribe(this)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sendPosition(position)
            }
        })
    }

    override fun syncReceive(position: Int) {
        viewPager.setCurrentItem(position, smoothScroll)
    }

}