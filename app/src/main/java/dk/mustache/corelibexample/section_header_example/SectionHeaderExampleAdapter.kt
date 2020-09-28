package dk.mustache.corelibexample.section_header_example

import dk.mustache.corelib.BR
import dk.mustache.corelib.adapters.DataBindingViewHolder
import dk.mustache.corelib.section_header_list.SectionHeaderListAdapter
import dk.mustache.corelib.section_header_list.SectionItem
import dk.mustache.corelibexample.R
import java.util.*

class SectionHeaderExampleAdapter (private val viewModelSectionExample: SectionHeaderExampleViewModel, val onShoppingListItemClicked: (shoppingListItem: SectionItem) -> Unit) : SectionHeaderListAdapter<SectionExampleItem, SectionHeaderItemExampleViewModel>(
    ArrayList(viewModelSectionExample.listWithSectionHeaders?:ArrayList()), R.layout.item_section_header_example, R.layout.item_section_header_example, R.layout.section_header_item_custom_example, onShoppingListItemClicked) {

    override fun onBindViewHolder(holder: SectionHeaderViewHolder<SectionHeaderItemExampleViewModel>, position: Int) {
        val shoppingListItem = itemListWithHeaders[position]

        val binding = holder.binding

        with(holder) {
            val viewModel = SectionHeaderItemExampleViewModel(position==2)
            bindViewModel(viewModel)
        }

        super.onBindViewHolder(holder,position)
    }
}