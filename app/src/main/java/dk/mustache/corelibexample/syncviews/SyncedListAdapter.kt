package dk.mustache.corelibexample.syncviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelibexample.R

class SyncedListAdapter(private val itemsCount: Int): RecyclerView.Adapter<SyncedListAdapter.SyncViewHolder>() {

    private lateinit var inflater: LayoutInflater

    private var selectedCallback: ((Int) -> Unit)? = null
    private var selectedPos: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyncViewHolder {
        if(!this::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        val view = inflater.inflate(R.layout.item_tall, parent, false)
        return SyncViewHolder(view)
    }

    override fun onBindViewHolder(holder: SyncViewHolder, position: Int) {
        holder.itemView.isSelected = position == selectedPos
        holder.txtView.text = (position + 1).toString()
        holder.itemView.setOnClickListener {
            notifyItemChanged(selectedPos)
            selectedPos = holder.bindingAdapterPosition
            notifyItemChanged(selectedPos)
            selectedCallback?.invoke(selectedPos)
        }
    }

    override fun getItemCount(): Int {
        return itemsCount
    }

    fun setSelectedCallback(callback: (Int) -> Unit) {
        selectedCallback = callback
    }

    class SyncViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtView: TextView = itemView.findViewById(R.id.txtItemNumber)
    }
}