package dk.mustache.corelib.syncviews

import kotlin.reflect.KFunction2

interface SyncSubject<T> {
    var sendEvent: KFunction2<SyncSubject<T>, T, Unit>?

    fun syncReceive(event: T)

    var eventIsOnGoing: Boolean
    fun eventStart() {
        eventIsOnGoing = true
    }
    fun eventComplete() {
        eventIsOnGoing = false
    }
}