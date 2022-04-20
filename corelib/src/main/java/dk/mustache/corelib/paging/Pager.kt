package dk.mustache.corelib.paging

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Connect to a data source and acquire data for a a specific page
 * number and page size.
 *
 * Use [addPagingActionListener] to listen to the data returned.
 *
 * Either use [loadPage] to load a single page.
 *
 * Or use [loadContinuous] to load pages until the last page is reached.
 *
 * If [mapper] is set, the acquired data will be converted using the
 * mapper before it is returned, otherwise the data is returned as
 * is.
 *
 * Alternatively mapper can be set after construction with the
 * [setMapper] function.
 *
 * @param R Type of the response supplied by the data source.
 * @param I Type of the items returned by the PagingActionListener.
 * @param mapper Converts response to list of items<I>. If null the
 *     reponse's data is returned unmapped.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class Pager<R : Pager.PagingResponse<*>, I>(mapper: ((R) -> List<I>)? = null) {

    @Suppress("UNCHECKED_CAST")
    private var mapper: (R) -> List<I> = { response -> response.data as List<I> }
    fun setMapper(mapper: (R) -> List<I>) {
        this.mapper = mapper
    }

    init {
        if (mapper != null) {
            this.mapper = mapper
        }
    }

    private val listeners = mutableListOf<PagingActionListener<I>>()
    private lateinit var disposableObserver: DisposableObserver<R>

    fun addPagingActionListener(listener: PagingActionListener<I>) {
        listeners.add(listener)
    }

    fun removePagingActionListener(listener: PagingActionListener<I>) {
        listeners.remove(listener)
    }

    /** Clears all PagingActionListeners */
    fun clearPagingActionListeners() {
        listeners.clear()
    }

    /** Cancels any ongoing calls to the repo */
    internal fun cancel() {
        if (this::disposableObserver.isInitialized) {
            disposableObserver.dispose()
        }
        listeners.forEach { it.onCancel() }
    }

    /**
     * Load a single page matching the given page and pageSize from the
     * given datasource.
     */
    internal fun loadPage(
        rxCall: (page: Int, pageSize: Int) -> Observable<R>,
        page: Int,
        pageSize: Int = 10,
    ) {
        loadPage({ p -> rxCall(p, pageSize) }, page, pageSize)
    }

    /**
     * Load a single page matching the given page and pageSize from the
     * given datasource.
     */
    internal fun loadPage(
        rxCall: (page: Int) -> Observable<R>,
        page: Int,
        pageSize: Int = 10,
    ) {
        subscribeCall(rxCall(page), page, pageSize)
    }

    /**
     * Continuously load one page at a time from the given datasource
     * until no more pages remain.
     */
    internal fun loadContinuous(
        rxCall: (page: Int, pageSize: Int) -> Observable<R>,
        startPage: Int = 0,
        pageSize: Int = 10,
        predefinedTotalPages: Int? = null
    ) {
        loadContinuous(
            { page -> rxCall(page, pageSize) },
            startPage,
            pageSize,
            predefinedTotalPages
        )
    }

    /**
     * Continuously load one page at a time from the given datasource
     * until no more pages remain.
     */
    internal fun loadContinuous(
        rxCall: (page: Int) -> Observable<R>,
        startPage: Int = 0,
        pageSize: Int = 10,
        predefinedTotalPages: Int? = null
    ) {
        subscribeCall(
            rxCall(startPage),
            startPage,
            pageSize,
            predefinedTotalPages
        ) { totalPagesFromCall ->
            loadContinuousLoop(
                rxCall,
                startPage + 1,
                pageSize,
                predefinedTotalPages ?: totalPagesFromCall
            )
        }
    }

    private fun loadContinuousLoop(
        rxCall: (page: Int) -> Observable<R>,
        page: Int,
        pageSize: Int,
        totalPages: Int
    ) {
        subscribeCall(rxCall(page), page, pageSize, totalPages) {
            // disposableObserver only sends a callback if there are more pages, so this loop
            // will automatically end when disposableObserver doesn't send a callback
            loadContinuous(rxCall, page + 1, pageSize, totalPages)
        }
    }

    private fun subscribeCall(
        observable: Observable<R>,
        startPage: Int,
        pageSize: Int,
        totalNumberOfHits: Int? = null,
        onDataReturned: ((totalHits: Int) -> Unit)? = null
    ) {
        createNewDisposableObserver(startPage, pageSize, totalNumberOfHits, onDataReturned)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(disposableObserver)
    }

    private fun createNewDisposableObserver(
        currentPage: Int,
        pageSize: Int,
        totalPages: Int?,
        onDataReturned: ((totalHits: Int) -> Unit)? = null
    ) {
        disposableObserver = object : DisposableObserver<R>() {

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                listeners.forEach { it.onError(e) }
            }

            override fun onNext(response: R) {
                disposableObserver.dispose()
                val actualTotalPages = totalPages ?: response.totalPages

                if (currentPage == 0) {
                    listeners.forEach { it.onTotalPagesAcquired(actualTotalPages) }
                }

                if (actualTotalPages > 0) {
                    if ((currentPage + 1) * pageSize >= actualTotalPages) { // If we are on the last page
                        listeners.forEach { it.onFinished(currentPage, mapper(response)) }
                    } else { // If we are on a page before the last page
                        listeners.forEach { it.onPageDownloaded(currentPage, mapper(response)) }
                        onDataReturned?.invoke(actualTotalPages)
                    }
                } else { // If there are no items
                    listeners.forEach { it.onFinished(currentPage, null) }
                }
            }
        }
    }

    /**
     * Interface for data source responses that can be parsed by the
     * [Pager].
     *
     * @param T The type of the data being returned by the data source.
     */
    interface PagingResponse<T> {
        val totalPages: Int
        val data: List<T>
    }

    abstract class PagingActionListener<T> {
        /**
         * Returns the number of total pages reported by the data
         * source, when the first response is returned.
         */
        open fun onTotalPagesAcquired(totalPages: Int) {}

        /**
         * Returns new pages returned by the data source, when the
         * current page is not the last page of the data source.
         *
         * @param pageNumber
         * @param items
         */
        open fun onPageDownloaded(pageNumber: Int, items: List<T>?) {}

        /**
         * Returns new pages returned by the data source, when the
         * current page is the last page of the data source.
         *
         * @param pageNumber
         * @param items
         */
        open fun onFinished(pageNumber: Int, items: List<T>?) {}

        /**
         * Returns any errors reported by the data source Rx.
         */
        open fun onError(e: Throwable) {}

        /** Returns when the current data acquisition is manually cancelled. */
        open fun onCancel() {}
    }
}