package dk.mustache.corelib.paging

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.BR

@SuppressLint("NotifyDataSetChanged")
abstract class GenericPagingAdapter<T: GenericPagingAdapter.PagingAdapterItem>(
    private val viewResource: Int,
    private val loadingResource: Int
) : RecyclerView.Adapter<GenericPagingAdapter.GenericPagingAdapterViewHolder<T>>() {
    var layoutInflater: LayoutInflater? = null

    val items: MutableList<PagingAdapterItem> = mutableListOf()

    var awaitingLoadingItems = true
    private var replaceItemsIndex = 0

    fun createLoadingItems(totalItemCount: Int) {
        if (awaitingLoadingItems) {
            awaitingLoadingItems = false
            for (i in 0..totalItemCount) {
                val pagingItem = PagingAdapterItem(true)
                items.add(pagingItem)
            }
            notifyDataSetChanged()
        }
    }

    fun replaceLoadingItems(i: List<T>) {
        val startIndex = replaceItemsIndex
        i.forEach {
            if (replaceItemsIndex < items.size) {
                items[replaceItemsIndex] = it
                replaceItemsIndex++
            }
        }
        notifyItemRangeChanged(startIndex, replaceItemsIndex - startIndex)
    }

    fun getLoadedItem(position: Int): T? {
        try {
            return items[position] as T
        }
        catch (e: ClassCastException) {
            return null
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenericPagingAdapterViewHolder<T> {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ViewDataBinding = if (viewType == 0) {
            DataBindingUtil.inflate(layoutInflater!!, loadingResource, parent, false)
        } else {
            DataBindingUtil.inflate(layoutInflater!!, viewResource, parent, false)
        }
        return GenericPagingAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenericPagingAdapterViewHolder<T>, position: Int) {}

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].isLoadingItem) 0 else 1
    }

    class GenericPagingAdapterViewHolder<T>(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindViewModel(itemViewModel: T) {
            binding.setVariable(BR.viewModel, itemViewModel)
            binding.executePendingBindings()
        }
    }

    open class PagingAdapterItem(var isLoadingItem: Boolean = false)
}