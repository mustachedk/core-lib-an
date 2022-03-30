package dk.mustache.corelib.syncviews

open class PositionSyncViewsViewModel : SyncViewsViewModel<SyncViewEvent>() {
    override fun sendSyncEvent(event: SyncViewEvent, id: Int) {
        super.sendSyncEvent(event, id)
    }

    override fun addEventListener(id: Int, eventListener: SyncEventListener<SyncViewEvent>?) {
        super.addEventListener(id, eventListener)
    }
}