package dk.mustache.corelibexample.syncviews

import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.selectable_list.SelectableAdapter
import dk.mustache.corelib.selectable_list.SelectableAdapterSettings
import dk.mustache.corelib.selectable_list.SelectableItem
import dk.mustache.corelibexample.databinding.ItemTallBinding

class SyncedSelectableListAdapter(
    private val pages: List<Item>,
    onItemSelectionToggled: (item: Item, selected: Boolean) -> Unit,
    settings: SelectableAdapterSettings
) : SelectableAdapter<Item>(pages, onItemSelectionToggled, settings) {

    override fun onBindViewHolder(rawHolder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(rawHolder, position)

        val holder = rawHolder as SelectableAdapter<Item>.SelectableListItemViewHolder
        val item = items[position]
        val binding = holder.binding
        if (binding is ItemTallBinding) {
            binding.txtItemNumber.text = (position + 1).toString()
            //TODO whatever
        }

    }
}

class Item(
    val index: Int,
    selected: Boolean = false
) : SelectableItem(
    index.toString(),
    "Page" + index,
    selected
)