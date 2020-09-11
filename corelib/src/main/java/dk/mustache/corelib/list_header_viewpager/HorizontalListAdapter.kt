package dk.mustache.corelib.list_header_viewpager

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.BR
import dk.mustache.corelib.R
import dk.mustache.corelib.utils.getScreenWidth
import dk.mustache.corelib.utils.toPx


class HorizontalListAdapter(
    val context: Context,
    private val productGroupSelectionListener: ProductGroupSelectionListener,
    var selectedIndex: Int,
    val settings: HeaderListViewPagerSettings,
    val screenWidth: Int,
    val listItemLayout: Int = R.layout.top_list_item,
    private val isShoppingList: Boolean = false,
    var hasScrolled: Boolean = false
) : ListAdapter<PageData, HorizontalListAdapter.HorizontalViewHolder>(OfferTypeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalViewHolder {

        val viewHolder = HorizontalViewHolder(
            when (viewType) {
                R.layout.item_category_list_first -> {
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_category_list_first, parent, false
                    )
                }
                else -> {
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        listItemLayout, parent, false
                    )
                }
            }
        )
        when (settings.type) {
            HeaderListViewPagerTypeEnum.STRETCH -> {
                //parent.measuredHeight does not work with wrap_content
                val height: Int = parent.measuredHeight
                val width: Int =  (screenWidth / itemCount) - (10.toPx() / itemCount)
                viewHolder.itemView.setLayoutParams(RecyclerView.LayoutParams(width, height))
            }
            else -> {

            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: HorizontalViewHolder, position: Int) {
        getItem(position).let { pageData ->
            with(holder) {
                bind(pageData, productGroupSelectionListener, position, selectedIndex)
                if(position==0 && !isShoppingList && hasScrolled) {
                    holder.itemView.setPadding(10.toPx(), 0, 0, 0)
                    holder.viewModel.paddingEnd.set(0)
                } else {
                    if (position==itemCount-1) {
                        holder.itemView.setPadding(10.toPx(), 0, 0, 0)
                        if (settings.type== dk.mustache.corelib.list_header_viewpager.HeaderListViewPagerTypeEnum.STRETCH) {
                            holder.viewModel.paddingEnd.set(0.toPx())
                        } else {
                            holder.viewModel.paddingEnd.set(100.toPx())
                        }
                    } else {
                        holder.itemView.setPadding(10.toPx(), 0, 0, 0)
                        holder.viewModel.paddingEnd.set(0)
                    }
                }
                itemView.tag = pageData
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position==0 && isShoppingList) {
            R.layout.item_category_list_first
        } else {
            super.getItemViewType(position)
        }

    }

    class HorizontalViewHolder(
        val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        lateinit var viewModel: HorizontalListItemViewModel

        fun bind(
            pageData: PageData,
            selectionListener: ProductGroupSelectionListener?,
            index: Int,
            selectedIndex: Int
        ) {

            viewModel = HorizontalListItemViewModel(
                pageData, selectionListener, index, selectedIndex
            )
            binding.setVariable(BR.viewModel, viewModel)
            binding.executePendingBindings()



        }
    }
}

interface ProductGroupSelectionListener {
    fun typeSelected(pageData: PageData, index: Int)
}

interface ViewUpdater {
    fun updateView()
}
