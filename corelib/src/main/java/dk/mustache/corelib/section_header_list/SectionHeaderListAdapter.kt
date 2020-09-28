package dk.mustache.corelib.section_header_list

import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.BR
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.SectionHeaderItemBinding

abstract class SectionHeaderListAdapter <T : SectionItem, U: ViewModel>  (val itemListWithHeaders: ArrayList<SectionItem>, val rowItemType: Int, val placeholderItemType: Int, val headerItemType: Int = R.layout.section_header_item, val onItemClicked: ((item: SectionItem) -> Unit)? = {}) : RecyclerView.Adapter<SectionHeaderListAdapter.SectionHeaderViewHolder<U>>() {
    private var layoutInflater: LayoutInflater? = null

    class SectionHeaderViewHolder<V>(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SectionItem) {
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()
        }

        fun bindViewModel(viewModel: V) {
            binding.setVariable(BR.viewModel, viewModel)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionHeaderViewHolder<U> {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding = when (viewType) {
            headerItemType -> {
                DataBindingUtil.inflate<ViewDataBinding>(layoutInflater!!, headerItemType, parent, false)
            }
            rowItemType -> {
                DataBindingUtil.inflate<ViewDataBinding>(layoutInflater!!, rowItemType, parent, false)
            }
            else -> {
                DataBindingUtil.inflate<ViewDataBinding>(layoutInflater!!, placeholderItemType, parent, false)
            }
        }

        return SectionHeaderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionHeaderViewHolder<U>, position: Int) {
        val item = itemListWithHeaders[position]

        holder.itemView.setOnClickListener {
            val lmda = onItemClicked
            if (lmda != null) {
                lmda(item)
            }
        }

        when (holder.binding) {
            is SectionHeaderItemBinding -> {
                holder.binding.sectionText.text = "${item.headerText}"
            }
            else -> {

            }
        }

        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemListWithHeaders.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = itemListWithHeaders[position]
        return if (item.isHeader!=true) {
            if (item.isPlaceholder==true) {
                placeholderItemType
            } else {
                rowItemType
            }
        } else {
            headerItemType
        }
    }

    /** Auto creates headers based on SectionItem.weight
     *  the items are sorted by weight */
    fun updateDataAndAddHeaders(newList: List<T>, sortDesc: Boolean = false) {
        setItems(newList)
        if (sortDesc) {
            itemListWithHeaders.sortByDescending {
                it.weight
            }
        } else {
            itemListWithHeaders.sortBy {
                it.weight
            }
        }

        var currentHeaderWeight = -1
        val createHeaderAtList = ArrayList<Pair<SectionItem, Int>>()
        itemListWithHeaders.forEachIndexed { index, item ->
            if (item.weight!=currentHeaderWeight && item.headerText!=null) {
                createHeaderAtList.add(Pair(item, index))
                currentHeaderWeight = item.weight
            }
        }

        var offset = 0
        try {
            createHeaderAtList.forEach {
                val sectionItem = itemListWithHeaders[it.second+offset]
                itemListWithHeaders.add(it.second+offset, SectionHeaderItem(sectionItem.headerText?:"", sectionItem.weight))
                offset++
            }
            notifyDataSetChanged()
        } catch(e: Exception) {

        }
    }

    fun setItems(newList: List<T>) {
        itemListWithHeaders.clear()
        itemListWithHeaders.addAll(newList)
    }

    fun updateData(newList: List<T>) {
        setItems(newList)
        notifyDataSetChanged()
    }

}
