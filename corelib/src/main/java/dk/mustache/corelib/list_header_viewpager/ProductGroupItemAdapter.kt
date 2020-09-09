package dk.mustache.corelib.list_header_viewpager

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.OfferTypeItemBinding
import dk.mustache.corelib.utils.toPx
import java.lang.Exception

class ProductGroupItemAdapter(
        val context: Context,
        private val productGroupSelectionListener: ProductGroupSelectionListener,
        var selectedIndex: Int,
        private val isShoppingList: Boolean = false,
        var hasScrolled: Boolean = false
) : ListAdapter<ProductGroup, ProductGroupItemAdapter.ViewHolder>(OfferTypeDiffCallback()), ViewUpdater {

    override fun updateView() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
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
                                R.layout.offer_type_item, parent, false
                        )
                    }
                },
                this
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { productGroup ->
            with(holder) {
                if(position==0 && !isShoppingList && hasScrolled) {

                } else {
                    holder.itemView.setPadding(10.toPx(), 0, 0, 0)
                }
                itemView.tag = productGroup
                bind(productGroup, productGroupSelectionListener, position, selectedIndex)
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

    class ViewHolder(
            private val binding: ViewDataBinding,
            val viewUpdater: ViewUpdater
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(productGroup: ProductGroup, productGroupSelectionListener: ProductGroupSelectionListener?, index: Int, selectedIndex: Int) {
            when (binding) {
                is OfferTypeItemBinding -> {
                    with(binding) {
                        if (productGroup.fieldExternalId?.toLowerCase()?.contains("og is")==true) {
                            productGroup.fieldExternalId = productGroup.fieldExternalId?.replace("og is", "& is")
                        }
//                        viewModel = OfferTypeItemViewModel(
//                                productGroup, viewUpdater, productGroupSelectionListener, index, selectedIndex
//                        )
                        executePendingBindings()
                    }
                }
                else -> {
                    binding.executePendingBindings()
                }
            }

        }
    }
}

interface ProductGroupSelectionListener {
    fun typeSelected(type: ProductGroup, index: Int)
}

interface ViewUpdater {
    fun updateView()
}
