package dk.mustache.corelib.syncviews

class PositionSyncConductor: SyncConductor<() -> Int>() {


    override fun transmitToOthers(transmitter: SyncSubject<() -> Int>, msg: () -> Int) {
        super.transmitToOthers(transmitter, msg)
    }
}