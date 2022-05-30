package dk.mustache.corelibexample.sandbox.selectable_list_example

import dk.mustache.corelib.selectable_list.SelectableItem

data class SelectableExampleItem(val id: String, val text: String, val defaultSelection: Boolean = false): SelectableItem(selectableId = id, selectableText = text, selected = defaultSelection)