package dk.mustache.corelib.validation.views

import android.content.Context
import android.util.AttributeSet
import android.widget.PopupMenu
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import dk.mustache.corelib.R

class ValidationMenu : ValidationTextView {
    private val onValidationChangedListeners: MutableList<(Boolean) -> Unit> = mutableListOf()

    private var viewModel: ValidationMenuViewModel? = null
    private lateinit var viewId: String
    fun setViewId(value: String) {
        viewId = value
    }

    override fun getViewIsValid(): Boolean? {
        return viewModel?.isValid
    }

    fun setIsValid(newValue: Boolean?) {
        viewModel?.apply { isValid = newValue }
    }

    private lateinit var popupMenu: PopupMenu

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @Suppress("UNUSED_PARAMETER")
    override fun init(context: Context, attrs: AttributeSet?) {
        super.init(context, attrs)
        if(id != NO_ID) {
            viewId = id.toString()
        }

        setOnClickListener {
            if (this::popupMenu.isInitialized) {
                popupMenu.show()
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewModel = ViewModelProvider(context as ViewModelStoreOwner).get(
            viewId,
            ValidationMenuViewModel::class.java
        )
    }

    fun setAdapter(adapter: ValidationMenuAdapter<*>?) {
        viewModel?.adapter = adapter
        if (viewModel != null && adapter != null) {
            // Update Menu on Changes
            adapter.setOnMenuChangedListener { titles ->
                if (titles != null && titles.isNotEmpty()) {
                    createMenu(titles)
                }
            }
            adapter.setOnSelectedTitleChangedListener { selected ->
                text = selected
                val result = requireNotNull(viewModel).validateValue()
                if (result.triggerCallbacks) {
                    triggerOnValidationChangedListener(result.value)
                }
            }

            // Initialize the data by triggering the listeners
            adapter.onMenuChanged()
            if(text.toString() != adapter.getSelectedTitle()) {
                adapter.onSelectedTitleChanged()
            }
        }
    }

    private fun createMenu(menuItems: List<String>) {
        popupMenu = PopupMenu(context, this)
        popupMenu.menuInflater.inflate(R.menu.empty_menu, popupMenu.menu)

        menuItems.forEach {
            popupMenu.menu.add(it)
        }

        popupMenu.setOnMenuItemClickListener {
            val title = it.title.toString()
            viewModel?.adapter?.setSelectedItem(title)
            // setSelectedItem triggers onSelectedTitleChangedListener above (in setAdapter)
            // if the text is different from the current text
            true
        }
    }

    private fun triggerOnValidationChangedListener(newValue: Boolean) {
        onValidationChangedListeners.forEach { it.invoke(newValue) }
    }

    override fun addOnValidationChangedListener(listener: (Boolean) -> Unit) {
        onValidationChangedListeners.add(listener)
    }

    companion object {
        @BindingAdapter("validationType")
        @JvmStatic
        fun setValidationType(view: ValidationMenu, newValue: Int?) {
            if (newValue != null && view.viewModel != null) {
                requireNotNull(view.viewModel).setValidationType(newValue)
            }
        }

        @InverseBindingAdapter(attribute = "isValid")
        @JvmStatic
        fun getViewIsValid(view: ValidationMenu): Boolean? {
            return view.getViewIsValid()
        }

        @BindingAdapter("isValid")
        @JvmStatic
        fun setIsValid(view: ValidationMenu, newValue: Boolean?) {
            view.setIsValid(newValue)
        }

        @BindingAdapter("app:isValidAttrChanged")
        @JvmStatic
        fun setListeners(view: ValidationMenu, attrChange: InverseBindingListener) {
            view.addOnValidationChangedListener { attrChange.onChange() }
        }
    }
}

class MenuItem<T>(val title: String, val item: T?) {
    companion object {
        operator fun invoke(title: String): MenuItem<String> {
            return MenuItem(title, title)
        }
    }
}