package dk.mustache.corelibexample.paging

import dk.mustache.corelib.paging.GenericPagingAdapter

class PagingAdapter(viewResource: Int, loadingResource: Int) :
    GenericPagingAdapter<PokePagingItem>(viewResource, loadingResource) {

    override fun onBindViewHolder(
        holder: GenericPagingAdapterViewHolder<PokePagingItem>,
        position: Int
    ) {
        if (items[position] is PokePagingItem) {
            holder.bindViewModel(items[position] as PokePagingItem)
        }
        super.onBindViewHolder(holder, position)
    }
}

