package dk.mustache.corelib.selectable_list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.ItemSelectablePickerBinding
import dk.mustache.corelib.BR
import dk.mustache.corelib.MustacheCoreLib
import dk.mustache.corelib.utils.toPx


abstract class SelectableAdapter<T : SelectableItem>(val items: List<T>, val onItemSelectionToggled: (item: T, selected: Boolean) -> Unit, val settings: SelectableAdapterSettings) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var layoutInflater: LayoutInflater? = null

    inner class SelectableListItemViewHolder(val itemSelectablePickerBinding: ItemSelectablePickerBinding, val binding: ViewDataBinding) : RecyclerView.ViewHolder(itemSelectablePickerBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }

        val selectablePickerBinding = DataBindingUtil.inflate<ItemSelectablePickerBinding>(layoutInflater!!, R.layout.item_selectable_picker, parent, false)
        var binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater!!, settings.layoutResId, parent, false)

        return SelectableListItemViewHolder(selectablePickerBinding, binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is SelectableAdapter<*>.SelectableListItemViewHolder) {

            val childLayoutBinding = holder.binding

            val parentLayout: FrameLayout = holder.itemSelectablePickerBinding.selectableItemLayout
            parentLayout.removeView(childLayoutBinding.root)

            parentLayout.addView(childLayoutBinding.root, 0)

            val params = holder.itemSelectablePickerBinding.checkedImage.layoutParams as ConstraintLayout.LayoutParams
            params.marginEnd = settings.endMargin.toPx()

            val item = items[position]
            childLayoutBinding.setVariable(BR.item, item)
            childLayoutBinding.executePendingBindings()

            holder.itemSelectablePickerBinding.selectableItemLayout.setOnClickListener {
                if (settings.singleSelection) {
                    deselectAll()
                }
                item.selected = !item.selected

                onItemSelectionToggled(item, item.selected)
                notifyDataSetChanged()
            }

            if (item.selected) {
                val checkedDrawable = ContextCompat.getDrawable(MustacheCoreLib.getContextCheckInit(), settings.selectedIcon)
                holder.itemSelectablePickerBinding.checkedImage.setImageDrawable(checkedDrawable)
            } else {
                val uncheckedDrawable = ContextCompat.getDrawable(MustacheCoreLib.getContextCheckInit(), settings.unselectedIcon)
                holder.itemSelectablePickerBinding.checkedImage.setImageDrawable(uncheckedDrawable)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun isAllSelected(): Boolean {
        var allSelected = true
        items.forEachIndexed { index, s ->
            if(!s.selected) {
                allSelected = false
            }
        }
        return allSelected
    }

    fun deselectAll() {
        items.forEach { item ->
            item.selected = false
        }
    }

    fun selectAll() {
        items.forEach { item ->
            item.selected = true
        }
    }

    fun toggleAllSelection() {
        if (isAllSelected()) {
            deselectAll()
        } else {
            selectAll()
        }
        notifyDataSetChanged()
    }
}