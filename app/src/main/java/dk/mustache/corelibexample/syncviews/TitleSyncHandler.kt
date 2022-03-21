package dk.mustache.corelibexample.syncviews

import android.widget.TextView
import dk.mustache.corelib.syncviews.PositionSyncConductor
import dk.mustache.corelib.syncviews.PositionSyncSubject
import dk.mustache.corelib.syncviews.SyncSubject
import kotlin.reflect.KFunction2

/**
 * Created primarily to demonstrate a SyncHandler that doesn't send any position updates
 * but only receives them.
 */
class TitleSyncHandler(private val textView: TextView, conductor: PositionSyncConductor): PositionSyncSubject {
    override var sendEvent: KFunction2<SyncSubject<() -> Int>, () -> Int, Unit>? = null
    override var eventIsOnGoing: Boolean = false

    init {
        conductor.subscribe(this)
    }

    override fun syncReceive(position: Int) {
        textView.text = "Page " + (position + 1)
    }
}