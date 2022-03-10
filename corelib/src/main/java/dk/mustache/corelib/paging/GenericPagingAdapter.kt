package dk.mustache.corelib.paging

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.BR
import dk.mustache.corelib.paging.GenericPagingAdapter.GenericPagingAdapterViewHolder

/**
 * Generic adapter for loading items in batches, adding new batches to
 * the bottom of the list.
 *
 * Includes support for loading-cells ([LoadingItem]s), which are
 * automatically replaced when new items are added (with [addItems]
 * function).
 *
 * @property viewResource Layout for item cells.
 * @property loadingResource Layout for [LoadingItem] cells.
 */
open class GenericPagingAdapter<T : GenericPagingAdapter.PagingAdapterItem>(
    @LayoutRes private val viewResource: Int,
    @LayoutRes private val loadingResource: Int
) : RecyclerView.Adapter<GenericPagingAdapterViewHolder<T>>() {
    lateinit var layoutInflater: LayoutInflater

    protected val items: MutableList<PagingAdapterItem> = mutableListOf()

    private var awaitingLoadingItems = true
    private var addItemsIndex = 0

    /**
     * Create a number of [LoadingItem]s. This function is only called
     * once, with all following calls ignored.
     *
     * @param count Number of [LoadingItem]s to add
     */
    @SuppressLint("NotifyDataSetChanged")
    fun createLoadingItems(count: Int) {
        if (awaitingLoadingItems) {
            awaitingLoadingItems = false
            addLoadingItems(count)
        }
    }

    /**
     * Create a number of [LoadingItem]s. This function can be called
     * multiple times, with all [LoadingItem]s added to the end of the
     * list.
     *
     * @param count Number of [LoadingItem]s to add
     */
    fun addLoadingItems(count: Int) {
        val startIndex = addItemsIndex
        val lastIndex = addItemsIndex + count
        for (i in startIndex until lastIndex) {
            val pagingItem = LoadingItem()
            items.add(pagingItem)
        }
        for (i in startIndex until lastIndex) {
            notifyItemInserted(i)
        }
    }

    /**
     * Add the supplied [PagingAdapterItem]s to the list. The items
     * are added to the bottom of the conceptual list (I.E. the list
     * discounting any [LoadingItem]s.
     *
     * If any [LoadingItem]s have been previously added, a number
     * matching the number of new items are replaced with the new items.
     *
     * @param newItems [PagingAdapterItem]s to add.
     */
    fun addItems(newItems: List<T>) {
        val startIndex = addItemsIndex
        newItems.forEach {
            if (addItemsIndex >= items.size) {
                items.add(it)
                notifyItemChanged(addItemsIndex)
            }
            else {
                items[addItemsIndex] = it
            }
            addItemsIndex++
        }
        notifyItemRangeChanged(startIndex, addItemsIndex - startIndex)
    }

    /**
     * Get an item from the adapter. If the given position is filled
     * with a LoadingItem, or is greater than the number of items in the
     * adapter, null is returned.
     */
    @Suppress("UNCHECKED_CAST")
    fun getLoadedItem(position: Int): T? {
        return if (position < items.size && items[position] !is LoadingItem) {
            items[position] as T
        } else {
            null
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

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: GenericPagingAdapterViewHolder<T>, position: Int) {
        if (holder is LoadedViewHolder) {
            holder.bindViewModel(items[position] as T)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is LoadingItem) 0 else 1
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

    private class LoadingItem : PagingAdapterItem
    /** Items loaded in this adapter must extend this interface */
    interface PagingAdapterItem
}