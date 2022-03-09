package dk.mustache.corelib.paging

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

@Suppress("MemberVisibilityCanBePrivate", "unused")
class Pager<R : Pager.PagingResponse<*>, I>(mapper: ((R) -> List<I>)? = null) {

    private var mapper: (R) -> List<I> = { response -> response.data as List<I>}
    fun setMapper(mapper: (R) -> List<I>) {
        this.mapper = mapper
    }

    init {
        if (mapper != null) {
            this.mapper = mapper
        }
    }

    private val listeners = mutableListOf<PagingActionListener<I>>()
    lateinit var disposableObserver: DisposableObserver<R>

    fun addPagingActionListener(listener: PagingActionListener<I>) {
        listeners.add(listener)
    }

    fun removePagingActionListener(listener: PagingActionListener<I>) {
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
        call: (page: Int, pageSize: Int) -> Observable<R>,
        startPage: Int = 0,
        pageSize: Int = 10,
    ) {
        loadFirst({ page -> call(page, pageSize) }, startPage, pageSize)
    }

    fun loadFirst(
        call: (page: Int) -> Observable<R>,
        startPage: Int = 0,
        pageSize: Int = 10,
    ) {
        rxCall(call(startPage), startPage, pageSize)
    }

    fun loadPage(
        call: (page: Int, pageSize: Int) -> Observable<R>,
        page: Int,
        pageSize: Int = 10,
    ) {
        loadPage({ page -> call(page, pageSize) }, page, pageSize)
    }

    fun loadPage(
        call: (page: Int) -> Observable<R>,
        page: Int,
        pageSize: Int = 10,
    ) {
        rxCall(call(page), page, pageSize)
    }

    fun loadContinuous(
        retrofitCall: (page: Int, pageSize: Int) -> Observable<R>,
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
        call: (page: Int) -> Observable<R>,
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
        call: (page: Int) -> Observable<R>,
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

    interface PagingResponse<T> {
        val totalPages: Int
        val data: List<T>
    }

    abstract class PagingActionListener<T> {
        open fun onTotalPagesAcquired(totalPages: Int) {}
        open fun onPageDownloaded(pageNumber: Int, items: List<T>?) {}
        open fun onFinished(pageNumber: Int, items: List<T>?) {}
        open fun onError(e: Throwable) {}
        open fun onCancel() {}
    }
}