package dk.mustache.corelib.syncviews

interface EventSyncView {
    var viewModel: SyncViewsViewModel<*>?

    var syncListener: SyncViewsViewModel.SyncEventListener<*>
}