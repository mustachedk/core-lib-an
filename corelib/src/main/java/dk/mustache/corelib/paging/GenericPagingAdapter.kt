package dk.mustache.corelib.paging

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.BR
import dk.mustache.corelib.paging.GenericPagingAdapter.GenericPagingAdapterViewHolder
import io.reactivex.rxjava3.core.Observable

abstract class GenericPagingAdapter<
        T : Paging.PagingResponse<*>,
        U : GenericPagingAdapter.PagingAdapterItem
        >(
    private val viewResource: Int,
    private val loadingResource: Int,
    private val pagingLib: Paging<U, T>
) : RecyclerView.Adapter<GenericPagingAdapterViewHolder<U>>() {
    lateinit var layoutInflater: LayoutInflater

    val items: MutableList<PagingAdapterItem> = mutableListOf()

    var awaitingLoadingItems = true
    private var replaceItemsIndex = 0

    init {
        pagingLib.addPagingActionListener(object : Paging.PagingActionListener<U> {
            override fun onTotalPagesAcquired(totalPages: Int) {
                createLoadingItems(totalPages)
            }

            override fun onPageDownloaded(pageNumber: Int, items: List<U>?) {
                items?.let {
                    replaceLoadingItems(items)
                }
            }

            override fun onFinished(pageNumber: Int, items: List<U>?) {
                items?.let {
                    replaceLoadingItems(items)
                }
            }

            override fun onError(errorMessage: String) {}

            override fun onCancel() {}
        })
    }

    fun startLoading(
        call: (page: Int, pageSize: Int) -> Observable<T>,
        startPage: Int = 0,
        pageSize: Int = 10
    ) {
        pagingLib.cancel()
        pagingLib.loadContinuous(
            call, startPage, pageSize
        )
    }

    fun startLoading(
        call: (page: Int) -> Observable<T>,
        startPage: Int = 0,
        pageSize: Int = 10
    ) {
        pagingLib.cancel()
        pagingLib.loadContinuous(
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

    fun replaceLoadingItems(i: List<U>) {
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
    fun getLoadedItem(position: Int): U? {
        return if (items[position] is LoadingItem) {
            null
        } else {
            items[position] as U
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenericPagingAdapterViewHolder<U> {
        if (!this::layoutInflater.isInitialized) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        return if (viewType == 0) {
            val binding: ViewDataBinding = DataBindingUtil.inflate(
                layoutInflater,
                loadingResource,
                parent,
                false
            )
            LoadingViewHolder(binding)
        } else {
            val binding: ViewDataBinding = DataBindingUtil.inflate(
                layoutInflater,
                viewResource,
                parent,
                false)
            LoadedViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: GenericPagingAdapterViewHolder<U>, position: Int) {}

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

    private class LoadingItem : PagingAdapterItem(true)
    abstract class PagingAdapterItem(var isLoadingItem: Boolean = false)
}