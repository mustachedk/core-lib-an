package dk.mustache.corelib.validation.views

class ValidationMenuAdapter<T>(
    private var menuItems: List<MenuItem<T>>? = null,
    private var selectedItem: MenuItem<T>? = null
) {

    private var onMenuChangedListener: ((items: List<String>?) -> Unit)? = null
    private var onSelectedTitleChangedListener: ((selectedItem: String) -> Unit)? = null

    // All settting of menuItems and selectedItem should go through these two function
    // to ensure the changeListeners get called
    fun setMenuItems(items: List<MenuItem<T>>?) {
        if(menuItems != items) {
            menuItems = items
            onMenuChanged()
        }
    }
    fun setSelectedItem(item: MenuItem<T>?) {
        if(selectedItem != item) {
            selectedItem = item
            onSelectedTitleChanged()
        }

    }

    fun getSelectedTitle(): String? {
        return selectedItem?.title
    }

    fun setSelectedItem(title: String): Boolean {
        if (getSelectedTitle().equals(title).not()) {
            setSelectedItem(menuItems?.firstOrNull { it.title == title })
            return true
        }
        return false
    }

    fun getMenuItems(): List<MenuItem<T>>? {
        return menuItems
    }

    fun getTitles(): List<String>? {
        return menuItems?.map { it.title }
    }

    fun setOnMenuChangedListener(listener: (List<String>?) -> Unit) {
        onMenuChangedListener = listener
    }

    fun onMenuChanged() {
        onMenuChangedListener?.invoke(menuItems?.map { it.title })
    }

    fun setOnSelectedTitleChangedListener(listener: (String) -> Unit) {
        onSelectedTitleChangedListener = listener
    }

    fun onSelectedTitleChanged() {
        onSelectedTitleChangedListener?.invoke(selectedItem?.title ?: "")
    }

    companion object {
        operator fun invoke(
            menuItems: List<String>? = null,
            initialSelectedItem: String? = null
        ): ValidationMenuAdapter<String> {
            return ValidationMenuAdapter(
                menuItems?.map { MenuItem(it, it) },
                MenuItem(initialSelectedItem ?: "", initialSelectedItem)
            )
        }
    }
}