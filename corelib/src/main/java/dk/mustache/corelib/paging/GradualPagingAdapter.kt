package dk.mustache.corelib.paging

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.BR
import io.reactivex.rxjava3.core.Observable

abstract class GradualPagingAdapter<
        R : Pager.PagingResponse<*>,
        I : GradualPagingAdapter.PagingAdapterItem
        >(
    private val viewResource: Int,
    private val loadingResource: Int,
    private val pagerLib: Pager<R, I>,
    private val recyclerView: RecyclerView
) : RecyclerView.Adapter<GradualPagingAdapter.GenericPagingAdapterViewHolder<I>>() {
    var layoutInflater: LayoutInflater? = null

    val items: MutableList<PagingAdapterItem> = mutableListOf()

    var awaitingLoadingItems = true
    private var replaceItemsIndex = 0

    init {
        pagerLib.addPagingActionListener(object : Pager.PagingActionListener<I>() {
            override fun onTotalPagesAcquired(totalPages: Int) {
                createLoadingItems(totalPages)
            }

            override fun onPageDownloaded(pageNumber: Int, items: List<I>?) {
                items?.let {
                    replaceLoadingItems(items)
                }
            }

            override fun onFinished(pageNumber: Int, items: List<I>?) {
                items?.let {
                    replaceLoadingItems(items)
                }
            }

            override fun onError(e: Throwable) {}

            override fun onCancel() {}
        })

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if(recyclerView.canScrollVertically(1)) {
                    
                }
            }
        })
    }

    fun startLoading(
        call: (page: Int, pageSize: Int) -> Observable<R>,
        startPage: Int = 0,
        pageSize: Int = 10
    ) {
        pagerLib.cancel()
        pagerLib.loadFirst(
            call, startPage, pageSize
        )
    }

    fun startLoading(
        call: (page: Int) -> Observable<R>,
        startPage: Int = 0,
        pageSize: Int = 10
    ) {
        pagerLib.cancel()
        pagerLib.loadFirst(
            call, startPage, pageSize
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun createLoadingItems(totalItemCount: Int) {
        if (awaitingLoadingItems) {
            awaitingLoadingItems = false
            for (i in 0..totalItemCount) {
                val pagingItem = LoadingItem()
                items.add(pagingItem)
            }
            notifyDataSetChanged()
        }
    }

    fun replaceLoadingItems(i: List<I>) {
        val startIndex = replaceItemsIndex
        i.forEach {
            if (replaceItemsIndex < items.size) {
                items[replaceItemsIndex] = it
                replaceItemsIndex++
            }
        }
        notifyItemRangeChanged(startIndex, replaceItemsIndex - startIndex)
    }

    @Suppress("UNCHECKED_CAST")
    fun getLoadedItem(position: Int): I? {
        return if(items[position] is LoadingItem) {
            null
        }
        else {
            items[position] as I
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenericPagingAdapterViewHolder<I> {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        return if (viewType == 0) {
            val binding: ViewDataBinding =
                DataBindingUtil.inflate(layoutInflater!!, loadingResource, parent, false)
            LoadingViewHolder(binding)
        } else {
            val binding: ViewDataBinding =
                DataBindingUtil.inflate(layoutInflater!!, viewResource, parent, false)
            LoadedViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: GenericPagingAdapterViewHolder<I>, position: Int) {}

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].isLoadingItem) 0 else 1
    }

    protected class LoadingViewHolder<T>(binding: ViewDataBinding) :
        GenericPagingAdapterViewHolder<T>(binding)

    protected class LoadedViewHolder<T>(binding: ViewDataBinding) :
        GenericPagingAdapterViewHolder<T>(binding)

    abstract class GenericPagingAdapterViewHolder<T>(
        private val binding: ViewDataBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindViewModel(itemViewModel: T) {
            binding.setVariable(BR.viewModel, itemViewModel)
            binding.executePendingBindings()
        }
    }

    private class LoadingItem: PagingAdapterItem(true)
    abstract class PagingAdapterItem(var isLoadingItem: Boolean = false)
}