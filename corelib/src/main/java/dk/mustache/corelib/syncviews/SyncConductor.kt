package dk.mustache.corelib.syncviews

import android.os.Handler

open class SyncConductor<T> {
    private val subjects = mutableListOf<SyncSubject<T>>()

    fun getSubjects(): List<SyncSubject<T>> {
        return subjects.toList()
    }

    fun subscribe(subject: SyncSubject<T>) {
        subject.sendEvent = ::transmitToOthers
        subjects.add(subject)
    }

    fun unsubscribe(subject: SyncSubject<T>) {
        subject.sendEvent = null
        subjects.remove(subject)
    }

    protected open fun transmitToOthers(transmitter: SyncSubject<T>, msg: T) {
        subjects.forEach { it.eventStart() }
        subjects.filterNot { it == transmitter }.forEach { it.syncReceive(msg) }
        subjects.forEach { it.eventComplete() }
    }
}