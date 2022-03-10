package dk.mustache.corelib.paging

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.BR
import dk.mustache.corelib.paging.GenericPagingAdapter.GenericPagingAdapterViewHolder

open class GenericPagingAdapter<T : GenericPagingAdapter.PagingAdapterItem>(
    private val viewResource: Int,
    private val loadingResource: Int
) : RecyclerView.Adapter<GenericPagingAdapterViewHolder<T>>() {
    lateinit var layoutInflater: LayoutInflater

    val items: MutableList<PagingAdapterItem> = mutableListOf()

    var awaitingLoadingItems = true
    private var addItemsIndex = 0

    @SuppressLint("NotifyDataSetChanged")
    fun createLoadingItems(totalItemCount: Int) {
        if (awaitingLoadingItems) {
            awaitingLoadingItems = false
            addLoadingItems(totalItemCount)
        }
    }

    fun addLoadingItems(count: Int) {
        val startIndex = addItemsIndex
        val lastIndex = addItemsIndex + count
        for (i in startIndex until lastIndex) {
            val pagingItem = LoadingItem()
            items.add(pagingItem)
        }
        for(i in startIndex until lastIndex) {
            notifyItemInserted(i)
        }
    }

    fun replaceLoadingItems(i: List<T>) {
        val startIndex = addItemsIndex
        i.forEach {
            if (addItemsIndex < items.size) {
                items[addItemsIndex] = it
                addItemsIndex++
            }
        }
        notifyItemRangeChanged(startIndex, addItemsIndex - startIndex)
    }

//    fun addOrReplaceItems(i: List<T>) {
//        val startIndex = addItemsIndex
//        i.forEach {
//            if (addItemsIndex < items.size) {
//                items[addItemsIndex] = it
//                addItemsIndex++
//            } else {
//                items.add(it)
//                addItemsIndex++
//            }
//        }
//        notifyItemRangeChanged(startIndex, addItemsIndex - startIndex)
//    }

    @Suppress("UNCHECKED_CAST")
    fun getLoadedItem(position: Int): T? {
        return if (items[position] is LoadingItem) {
            null
        } else {
            items[position] as T
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenericPagingAdapterViewHolder<T> {
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
                false
            )
            LoadedViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: GenericPagingAdapterViewHolder<T>, position: Int) {
        if (holder is LoadedViewHolder) {
            holder.bindViewModel(items[position] as T)
        }
    }

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