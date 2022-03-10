package dk.mustache.corelib.paging

import io.reactivex.rxjava3.core.Observable

class ContinuousPagingAdapterDataLoader<
        R : Pager.PagingResponse<*>,
        P : GenericPagingAdapter.PagingAdapterItem
        >(
    private val pager: Pager<R, P>,
    private val itemCountCallback: (totalItemCount: Int) -> Unit,
    private val onNextCallback: (i: List<P>) -> Unit,
    call: ((page: Int, pageSize: Int) -> Observable<R>)? = null
) {
    private lateinit var call: (page: Int, pageSize: Int) -> Observable<R>

    init {
        if (call != null) {
            this.call = call
        }

        pager.addPagingActionListener(object : Pager.PagingActionListener<P>() {
            override fun onTotalPagesAcquired(totalPages: Int) {
                itemCountCallback(totalPages)
            }

            override fun onPageDownloaded(pageNumber: Int, items: List<P>?) {
                items?.let {
                    onNextCallback(items)
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
        if (call != null) {
            this.call = call
        }
        pager.cancel()
        pager.loadContinuous(this.call, startPage, pageSize)
    }
}