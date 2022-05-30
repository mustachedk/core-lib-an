package dk.mustache.corelibexample.selectablelist

import dk.mustache.corelib.selectable_list.SelectableAdapter
import dk.mustache.corelib.selectable_list.SelectableAdapterSettings
import dk.mustache.corelib.selectable_list.SelectableItem

class SelectableExampleAdapter(
    items: List<SelectableItem>,
    onSelected: (item: SelectableItem, selected: Boolean) -> Unit
) : SelectableAdapter<SelectableItem>(
    items, onSelected, SelectableAdapterSettings(
        singleSelection = false
    )
)