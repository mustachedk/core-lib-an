package dk.mustache.corelib.section_header_list

import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.R
import dk.mustache.corelib.adapters.DataBindingViewHolder
import dk.mustache.corelib.databinding.SectionHeaderItemBinding

abstract class SectionHeaderListAdapter <T : SectionItem, U: ViewModel>  (val itemListWithHeaders: ArrayList<T>, val rowItemType: Int, val placeholderItemType: Int, val onItemClicked: ((item: T) -> Unit)?, val headerItemType: Int = R.layout.section_header_item) : RecyclerView.Adapter<DataBindingViewHolder<T, U>>() {
    private var layoutInflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T, U> {
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

        return DataBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T, U>, position: Int) {
        val item = itemListWithHeaders[position]

        when (holder.binding) {
            is SectionHeaderItemBinding -> {
                holder.binding.sectionText.text = "${item.headerText}"
            }
            else -> {

            }
        }
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

    fun updateData(newList: List<T>) {
        itemListWithHeaders.clear()
        itemListWithHeaders.addAll(newList)
    }
}
