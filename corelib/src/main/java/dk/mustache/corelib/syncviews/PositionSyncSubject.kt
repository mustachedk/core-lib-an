package dk.mustache.corelib.syncviews

import kotlin.reflect.KFunction2

interface PositionSyncSubject : SyncSubject<() -> Int> {

    fun sendPosition(position: Int) {
        if(!eventIsOnGoing) {
            sendEvent?.invoke(this) { position }
        }
    }

    fun syncReceive(position: Int)

    override fun syncReceive(event: () -> Int) {
        syncReceive(event())
    }
}