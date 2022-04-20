package dk.mustache.corelibexample.paging

import android.util.Log
import dk.mustache.corelib.paging.GenericPagingAdapter
import dk.mustache.corelibexample.R

class ExtendedPagingAdapter : GenericPagingAdapter<PokemonPagingItem>(
    R.layout.paging_demo_loaded_item,
    R.layout.paging_demo_loading_item
) {

    override fun onBindViewHolder(
        holder: GenericPagingAdapterViewHolder<PokemonPagingItem>,
        position: Int
    ) {
        if(holder is LoadedViewHolder) {
            val item = items[position] as PokemonPagingItem
            holder.bindViewModel(item)
            Log.v("Test Tag", "Loaded item: ${item.id}")
        }
        super.onBindViewHolder(holder, position)
    }

}

data class PokemonPagingItem(val id: Int, val name: String, val attack: Int, val defense: Int) :
    GenericPagingAdapter.PagingAdapterItem {

    val text = "$id: $name (att: $attack, def: $defense)"
}

