package dk.mustache.corelib.paging

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

@Suppress("MemberVisibilityCanBePrivate", "unused")
class Paging<U, T : Paging.PagingResponse<*>>(private val mapper: (T) -> List<U>) {

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

    fun pagedRetrofitCall(
        retrofitCall: (startPage: Int, pageSize: Int) -> Observable<T>,
        startPage: Int,
        pageSize: Int,
        totalNumberOfHits: Int = 0
    ) {
        rxCall(retrofitCall(startPage, pageSize), startPage, pageSize, totalNumberOfHits) {
            pagedRetrofitCall(
                retrofitCall,
                startPage + 1,
                pageSize,
                if (it > 0) it else totalNumberOfHits
            )
        }
    }

    fun pagedRetrofitCall(
        text: String,
        retrofitCall: (str: String, startPage: Int, pageSize: Int) -> Observable<T>,
        startPage: Int,
        pageSize: Int,
        totalNumberOfHits: Int = 0
    ) {
        rxCall(retrofitCall(text, startPage, pageSize), startPage, pageSize, totalNumberOfHits) {
            pagedRetrofitCall(
                text,
                retrofitCall,
                startPage + 1,
                pageSize,
                if (it > 0) it else totalNumberOfHits
            )
        }
    }

    fun pagedRetrofitCall(
        text: String,
        text2: String,
        retrofitCall: (str: String, str2: String, startPage: Int, pageSize: Int) -> Observable<T>,
        startPage: Int,
        pageSize: Int,
        totalNumberOfHits: Int = 0
    ) {
        rxCall(
            retrofitCall(text, text2, startPage, pageSize),
            startPage,
            pageSize,
            totalNumberOfHits
        ) {
            pagedRetrofitCall(
                text,
                text2,
                retrofitCall,
                startPage + 1,
                pageSize,
                if (it > 0) it else totalNumberOfHits
            )
        }
    }

    private fun pagedRetrofitCall(
        text: String,
        text2: String,
        text3: String,
        retrofitCall: (str: String, str2: String, str3: String, startPage: Int, pageSize: Int) -> Observable<T>,
        startPage: Int,
        pageSize: Int,
        totalNumberOfHits: Int = 0
    ) {
        rxCall(
            retrofitCall(text, text2, text3, startPage, pageSize),
            startPage,
            pageSize,
            totalNumberOfHits
        ) {
            pagedRetrofitCall(
                text,
                text2,
                text3,
                retrofitCall,
                startPage + 1,
                pageSize,
                if (it > 0) it else totalNumberOfHits
            )
        }
    }

    private fun rxCall(
        observable: Observable<T>,
        startPage: Int,
        pageSize: Int,
        totalNumberOfHits: Int,
        onDataReturned: (totalHits: Int) -> Unit
    ) {
        createNewDisposableObserver(startPage, pageSize, totalNumberOfHits, onDataReturned)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(disposableObserver)
    }

    private fun createNewDisposableObserver(
        currentPage: Int,
        pageSize: Int,
        totalNumberOfHits: Int,
        onDataReturned: (totalHits: Int) -> Unit
    ) {
        disposableObserver = object : DisposableObserver<T>() {

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                listeners.forEach { it.onError(e.localizedMessage ?: "") }
            }

            override fun onNext(response: T) {
                disposableObserver.dispose()
                val totalHits = if (currentPage == 0) {
                    listeners.forEach { it.onTotalPagesAcquired(response.totalPages) }
                    response.totalPages
                } else {
                    totalNumberOfHits
                }

                if (totalHits > 0) {
                    if ((currentPage + 1) * pageSize >= totalHits) { // If we are on the last page
                        listeners.forEach { it.onFinished(currentPage, mapper(response)) }
                    } else { // If we are on a page before the last page
                        listeners.forEach { it.onPageDownloaded(currentPage, mapper(response)) }
                        onDataReturned(totalHits)
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