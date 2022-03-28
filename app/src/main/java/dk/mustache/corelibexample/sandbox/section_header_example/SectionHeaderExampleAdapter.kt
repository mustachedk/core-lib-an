package dk.mustache.corelibexample.sandbox.section_header_example

import androidx.databinding.ViewDataBinding
import dk.mustache.corelib.BR
import dk.mustache.corelib.adapters.DataBindingViewHolder
import dk.mustache.corelib.section_header_list.SectionHeaderListAdapter
import dk.mustache.corelib.section_header_list.SectionItem
import dk.mustache.corelib.sticky_header_decoration.StickyHeaderListAdapter
import dk.mustache.corelibexample.R
import dk.mustache.corelibexample.databinding.SectionHeaderItemCustomExampleBinding
import java.util.*

class SectionHeaderExampleAdapter (private val viewModelSectionExample: SectionHeaderExampleViewModel, val onShoppingListItemClicked: (shoppingListItem: SectionItem) -> Unit) : StickyHeaderListAdapter<SectionExampleItem, SectionHeaderItemExampleViewModel>(
    ArrayList(viewModelSectionExample.listWithSectionHeaders?:ArrayList()), R.layout.item_section_header_example, R.layout.item_section_header_example, R.layout.section_header_item_custom_example, onShoppingListItemClicked) {

    override fun onBindViewHolder(holder: SectionHeaderViewHolder<SectionHeaderItemExampleViewModel>, position: Int) {
        val shoppingListItem = itemListWithHeaders[position]

        val binding = holder.binding

        with(holder) {
            val viewModel = SectionHeaderItemExampleViewModel(position==4)
            bindViewModel(viewModel)
        }

        super.onBindViewHolder(holder,position)
    }

    override fun bindHeaderView(headerBinding: ViewDataBinding, position: Int) {

        val item = itemListWithHeaders[position]

        when (headerBinding) {
            is SectionHeaderItemCustomExampleBinding -> {
                headerBinding.viewModel = SectionHeaderItemExampleViewModel(position==4)
                headerBinding.item = item
            }
            else -> {

            }
        }

        super.bindHeaderView(headerBinding, position)
    }
}