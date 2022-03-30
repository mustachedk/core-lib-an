package dk.mustache.corelib.syncviews

open class SyncViewsViewModel<T> {

    var syncEventlisteners = mutableListOf<SyncEventListener<T>>()

    open fun addEventListener(id: Int, eventListener: SyncEventListener<T>?) {
        if (eventListener!=null) {
            eventListener.id = id
            syncEventlisteners.add(eventListener)
        }
    }

    fun removeEventListener(id: Int) {
        val eventIndexed = syncEventlisteners.withIndex().find { it.value.id == id }
        if (eventIndexed?.index!=null) {
            syncEventlisteners.removeAt(eventIndexed.index)
        }
    }

    open fun sendSyncEvent(event: T, id: Int = -1) {

        val matchingSyncEvents = syncEventlisteners.filter {
            it.id==id || id == -1
        }
        matchingSyncEvents.forEach {
            it.receiveEvent(event)
        }
    }

    interface SyncEventListener<T> {
        var id: Int
        fun receiveEvent(event: T?)
    }
}