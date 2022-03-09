package dk.mustache.corelib.paging

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

@Suppress("MemberVisibilityCanBePrivate", "unused")
class Paging<U, T : Paging.PagingResponse<*>>(mapper: ((T) -> List<U>)? = null) {

    private lateinit var mapper: (T) -> List<U>
    fun setMapper(mapper: (T) -> List<U>) {
        this.mapper = mapper
    }

    init {
        if (mapper != null) {
            this.mapper = mapper
        }
    }

    private val listeners = mutableListOf<PagingActionListener<U>>()
    lateinit var disposableObserver: DisposableObserver<T>

    fun addPagingActionListener(listener: PagingActionListener<U>) {
        listeners.add(listener)
    }

    fun removePagingActionListener(listener: PagingActionListener<U>) {
        listeners.remove(listener)
    }

    fun clearPagingActionListeners() {
        listeners.clear()
    }

    fun cancel() {
        if (this::disposableObserver.isInitialized) {
            disposableObserver.dispose()
        }
        listeners.forEach { it.onCancel() }
    }

    fun loadFirst(
        call: (page: Int, pageSize: Int) -> Observable<T>,
        startPage: Int = 0,
        pageSize: Int = 10,
    ) {
        loadFirst({ page -> call(page, pageSize) }, startPage, pageSize)
    }

    fun loadFirst(
        call: (page: Int) -> Observable<T>,
        startPage: Int = 0,
        pageSize: Int = 10,
    ) {
        rxCall(call(startPage), startPage, pageSize)
    }

    fun loadPage(
        call: (page: Int, pageSize: Int) -> Observable<T>,
        page: Int,
        pageSize: Int = 10,
    ) {
        loadPage({ page -> call(page, pageSize) }, page, pageSize)
    }

    fun loadPage(
        call: (page: Int) -> Observable<T>,
        page: Int,
        pageSize: Int = 10,
    ) {
        rxCall(call(page), page, pageSize)
    }

    fun loadContinuous(
        retrofitCall: (page: Int, pageSize: Int) -> Observable<T>,
        startPage: Int = 0,
        pageSize: Int = 10,
        predefinedTotalPages: Int? = null
    ) {
        loadContinuous(
            { page -> retrofitCall(page, pageSize) },
            startPage,
            pageSize,
            predefinedTotalPages
        )
    }

    fun loadContinuous(
        call: (page: Int) -> Observable<T>,
        startPage: Int = 0,
        pageSize: Int = 10,
        predefinedTotalPages: Int? = null
    ) {
        rxCall(call(startPage), startPage, pageSize, predefinedTotalPages) { totalPagesFromCall ->
            loadContinuousLoop(
                call,
                startPage + 1,
                pageSize,
                predefinedTotalPages ?: totalPagesFromCall
            )
        }
    }

    private fun loadContinuousLoop(
        call: (page: Int) -> Observable<T>,
        page: Int,
        pageSize: Int,
        totalPages: Int
    ) {
        rxCall(call(page), page, pageSize, totalPages) {
            // disposableObserver only sends a callback if there are more pages, so this loop
            // will automatically end when disposableObserver doesn't send a callback
            loadContinuous(call, page + 1, pageSize, totalPages)
        }
    }

    private fun rxCall(
        observable: Observable<T>,
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
        disposableObserver = object : DisposableObserver<T>() {

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                listeners.forEach { it.onError(e.localizedMessage ?: "") }
            }

            override fun onNext(response: T) {
                if (!this@Paging::mapper.isInitialized) {
                    throw IllegalStateException("data mapper was not set before date was returned")
                }

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

    interface PagingResponse<T> {
        val totalPages: Int
        val data: List<T>
    }

    interface PagingActionListener<T> {
        fun onTotalPagesAcquired(totalPages: Int)
        fun onPageDownloaded(pageNumber: Int, items: List<T>?)
        fun onFinished(pageNumber: Int, items: List<T>?)
        fun onError(errorMessage: String)
        fun onCancel()
    }
}