package dk.mustache.corelib.paging

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class Paging<T>() {

    lateinit var listener: PagingActionListener<T>
    lateinit var disposableObserver: DisposableObserver<T>

    fun cancel() {
        if (this::disposableObserver.isInitialized) {
            disposableObserver.dispose()
        }
        if (this::listener.isInitialized) {
            listener.onCancel()
        }
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
                listener.onError(e.localizedMessage ?: "")
            }

            override fun onNext(response: T) {
                disposableObserver.dispose()
                val totalHits = if (currentPage == 0) {
                    listener.onTotalHitsResolved(response)
                } else {
                    totalNumberOfHits
                }

                if (totalHits > 0) {
                    if ((currentPage + 1) * pageSize >= totalHits) { // If we are on the last page
                        listener.onFinished(currentPage, response)
                    } else { // If we are on a page before the last page
                        listener.onPageDownloaded(currentPage, response)
                        onDataReturned(totalHits)
                    }
                } else { // If there are no items
                    listener.onFinished(currentPage, null)
                }
            }
        }
    }

    interface PagingActionListener<T> {
        fun onTotalHitsResolved(response: T): Int
        fun onPageDownloaded(pageNumber: Int, response: T?)
        fun onFinished(pageNumber: Int, response: T?)
        fun onError(errorMessage: String)
        fun onCancel()
    }
}