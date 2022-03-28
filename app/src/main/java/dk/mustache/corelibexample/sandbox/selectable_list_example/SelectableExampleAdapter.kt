package dk.mustache.corelibexample.sandbox.selectable_list_example

import androidx.databinding.ViewDataBinding
import dk.mustache.corelib.BR
import dk.mustache.corelib.adapters.DataBindingViewHolder
import dk.mustache.corelib.section_header_list.SectionHeaderListAdapter
import dk.mustache.corelib.section_header_list.SectionItem
import dk.mustache.corelib.selectable_list.SelectableAdapter
import dk.mustache.corelib.selectable_list.SelectableAdapterSettings
import dk.mustache.corelib.selectable_list.SelectableItem
import dk.mustache.corelib.sticky_header_decoration.StickyHeaderListAdapter
import dk.mustache.corelibexample.R
import dk.mustache.corelibexample.databinding.SectionHeaderItemCustomExampleBinding
import java.util.*

class SelectableExampleAdapter (items: List<SelectableItem>, selectedList: ArrayList<Int>, val onSelected: (item: SelectableItem, selected: Boolean) -> Unit) : SelectableAdapter<SelectableItem>(items, onSelected, SelectableAdapterSettings()) {

}