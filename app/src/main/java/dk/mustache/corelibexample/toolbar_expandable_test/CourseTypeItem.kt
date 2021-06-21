package dk.mustache.corelibexample.toolbar_expandable_test

import dk.mustache.corelib.selectable_list.SelectableItem

data class CourseTypeItem(val id: String, val name: String, val defaultSelected: Boolean, val filterCompleted: Boolean = false): SelectableItem(selectableId = id, selectableText = name, selected = defaultSelected)