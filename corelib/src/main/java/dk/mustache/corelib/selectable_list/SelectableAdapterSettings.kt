package dk.mustache.corelib.selectable_list

import dk.mustache.corelib.R

class SelectableAdapterSettings(
    val singleSelection: Boolean = true,
    val layoutResId: Int = R.layout.item_selectable_std_text,
    val selectedIcon: Int = R.drawable.ic_vector_selected,
    val unselectedIcon: Int = R.drawable.ic_vector_unselected,
    val endMargin: Int = 10)