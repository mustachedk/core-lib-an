package dk.mustache.corelibexample.interscroll

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import dk.mustache.corelib.interscroll.InterscrollView
import dk.mustache.corelib.utils.toPx
import dk.mustache.corelibexample.databinding.ItemListFillerBinding

class DemoAdapter(private val items: List<Any>, private val interscrollImageview: ImageView) :
    RecyclerView.Adapter<ViewHolder>() {

    companion object {
        const val OTHER_ITEMTYPE = 0
        const val INTERSCROLL_ITEMTYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            INTERSCROLL_ITEMTYPE -> InterscrollViewHolder(InterscrollView(parent.context))
            else -> FillerViewHolder(ItemListFillerBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is InterscrollViewHolder -> holder.view.setup(interscrollImageview, 200.toPx())
            is FillerViewHolder -> holder.binding.root.setBackgroundResource(item as Int)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        if (items[position] is InterscrollFragment.InterscrollItem) {
            return INTERSCROLL_ITEMTYPE
        } else {
            return OTHER_ITEMTYPE
        }
    }

    class InterscrollViewHolder(val view: InterscrollView) : ViewHolder(view)
    class FillerViewHolder(val binding: ItemListFillerBinding) : ViewHolder(binding.root)
}