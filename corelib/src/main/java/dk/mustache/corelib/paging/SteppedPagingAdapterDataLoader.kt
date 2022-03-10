package dk.mustache.corelib.paging

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Observable

class SteppedPagingAdapterDataLoader<
        R : Pager.PagingResponse<*>,
        P : GenericPagingAdapter.PagingAdapterItem
        >(
    private val pager: Pager<R, P>,
    private val addLoadingItemsCallback: (totalItemCount: Int) -> Unit,
    private val onNextCallback: (i: List<P>) -> Unit,
    call: ((page: Int, pageSize: Int) -> Observable<R>)? = null
) {
    private lateinit var call: (page: Int, pageSize: Int) -> Observable<R>

    val update = MutableLiveData<Int>()

    private var activePage = 0
    private var pageSize = 0
    private var totalPages = 0

    private lateinit var calc: PageCalc

    init {
        if (call != null) {
            this.call = call
        }

        pager.addPagingActionListener(object : Pager.PagingActionListener<P>() {
            override fun onTotalPagesAcquired(totalPages: Int) {
                this@SteppedPagingAdapterDataLoader.totalPages = totalPages
                addLoadingItemsCallback(pageSize)
            }

            override fun onPageDownloaded(pageNumber: Int, items: List<P>?) {
                items?.let {
                    val pagesLoaded = calc.numberItemsIncluding(pageNumber)
                    val numberLoadingItems = if(pagesLoaded + pageSize > totalPages) {
                        totalPages - pagesLoaded
                    }
                    else {
                        pageSize
                    }
                    onNextCallback(items)
                    addLoadingItemsCallback(numberLoadingItems)
                    update.value = pageNumber
                }
            }

            override fun onFinished(pageNumber: Int, items: List<P>?) {
                items?.let {
                    onNextCallback(items)
                }
            }

            override fun onError(e: Throwable) {}

            override fun onCancel() {}
        })
    }

    fun startLoading(
        call: ((page: Int, pageSize: Int) -> Observable<R>)? = null,
        startPage: Int = 0,
        pageSize: Int = 10
    ) {
        calc = PageCalc(pageSize)
        if (call != null) {
            this.call = call
        }
        pager.cancel()
        pager.loadFirst(this.call, startPage, pageSize)
        activePage = startPage + 1
        this.pageSize = pageSize
    }

    fun loadMore() {
        Log.wtf("test", "Loading Page: $activePage")
        pager.loadPage(call, activePage, pageSize)
        activePage++
    }
}