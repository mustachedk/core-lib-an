package dk.mustache.corelibexample.syncviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.selectable_list.SelectableAdapter
import dk.mustache.corelib.selectable_list.SelectableAdapterSettings
import dk.mustache.corelib.selectable_list.SelectableItem
import dk.mustache.corelibexample.R

class SyncedSelectableListAdapter(
    private val pages: List<Item>,
    onItemSelectionToggled: (item: Item, selected: Boolean) -> Unit,
    settings: SelectableAdapterSettings
) : SelectableAdapter<Item>(pages, onItemSelectionToggled, settings) {

    private lateinit var inflater: LayoutInflater

    private var selectedCallback: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyncViewHolder {
        if (!this::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        val view = inflater.inflate(R.layout.item_tall, parent, false)
        return SyncViewHolder(view)
    }

    override fun onBindViewHolder(rawHolder: RecyclerView.ViewHolder, position: Int) {
//        super.onBindViewHolder(holder, position)

        val holder = rawHolder as SyncViewHolder
        val item = items[position]

        holder.txtView.text = (position + 1).toString()
        holder.itemView.setOnClickListener {
            items.filter { it.selected }.forEach {
                item.selected = false
                notifyItemChanged(it.index)
            }

            item.selected = true
            onItemSelectionToggled(item, item.selected)
            notifyItemChanged(item.index)
        }

    }

    class SyncViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtView: TextView = itemView.findViewById(R.id.txtItemNumber)
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