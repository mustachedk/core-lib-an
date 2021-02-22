package dk.mustache.corelib.selectable_list

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.ItemSelectablePickerBinding
import dk.mustache.corelib.BR


abstract class SelectableAdapter<T>(val items: List<T>, val selectedValueList: ArrayList<Int>, val onItemSelectionToggled: (item: T, selected: Boolean) -> Unit, val singleSelection: Boolean = true, val layoutResId: Int = R.layout.item_selectable_std_text) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var layoutInflater: LayoutInflater? = null

    inner class SelectableListItemViewHolder(val itemSelectablePickerBinding: ItemSelectablePickerBinding, val customResBinding: ViewDataBinding) : RecyclerView.ViewHolder(itemSelectablePickerBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }

        val selectablePickerBinding = DataBindingUtil.inflate<ItemSelectablePickerBinding>(layoutInflater!!, R.layout.item_selectable_picker, parent, false)
        var binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater!!, layoutResId, parent, false)

        return SelectableListItemViewHolder(selectablePickerBinding, binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is SelectableAdapter<*>.SelectableListItemViewHolder) {

            val childLayoutBinding = holder.customResBinding

            val parentLayout: ConstraintLayout = holder.itemSelectablePickerBinding.selectableItemLayout
            parentLayout.removeView(childLayoutBinding.root)
            val set = ConstraintSet()

            parentLayout.addView(childLayoutBinding.root, 0)

            set.clone(parentLayout)

            set.connect(
                childLayoutBinding.root.id,
                ConstraintSet.TOP,
                parentLayout.id,
                ConstraintSet.TOP,
                0
            )

            set.connect(
                childLayoutBinding.root.id,
                ConstraintSet.BOTTOM,
                parentLayout.id,
                ConstraintSet.BOTTOM,
                0
            )

            set.connect(
                childLayoutBinding.root.id,
                ConstraintSet.START,
                parentLayout.id,
                ConstraintSet.START,
                0
            )

            set.connect(
                childLayoutBinding.root.id,
                ConstraintSet.END,
                parentLayout.id,
                ConstraintSet.END,
                0
            )

            set.applyTo(parentLayout)

            val item = items[position]
            childLayoutBinding.setVariable(BR.item, item)
            childLayoutBinding.executePendingBindings()

            holder.itemSelectablePickerBinding.selectableItemLayout.setOnClickListener {
                if (singleSelection) {
                    selectedValueList.clear()
                    selectedValueList.add(position)
                } else {
                    if (selectedValueList.contains(position)) {
                        selectedValueList.remove(position)
                    } else {
                        selectedValueList.add(position)
                    }
                }
                onItemSelectionToggled(item, selectedValueList.contains(position))
                notifyDataSetChanged()
            }

            if (selectedValueList.contains(position)) {
                holder.itemSelectablePickerBinding.categoryChecked.visibility = View.VISIBLE
            } else {
                holder.itemSelectablePickerBinding.categoryChecked.visibility = View.INVISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun isAllSelected(): Boolean {
        var allSelected = true
        items.forEachIndexed { index, s ->
            if(!selectedValueList.contains(index)) {
                allSelected = false
            }
        }
        return allSelected
    }

    fun deSelectAll() {
        items.forEachIndexed { index, s ->
            if(selectedValueList.contains(index)) {
                selectedValueList.remove(index)
            }
        }
    }

    fun selectAll() {
        items.forEachIndexed { index, s ->
            if(!selectedValueList.contains(index)) {
                selectedValueList.add(index)
            }
        }
    }

    fun toggleAllSelection() {
        if (isAllSelected()) {
            deSelectAll()
        } else {
            selectAll()
        }
        notifyDataSetChanged()
    }
}