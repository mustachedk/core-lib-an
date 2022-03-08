package dk.mustache.corelibexample.paging

import dk.mustache.corelib.paging.GenericPagingAdapter
import dk.mustache.corelib.paging.Paging
import dk.mustache.corelibexample.testdata.PokemonTestRepo

class PagingAdapter(
    viewResource: Int,
    loadingResource: Int,
    pagingLib: Paging<PokePagingItem, PokemonTestRepo.PokemonResponse>
) : GenericPagingAdapter<PokemonTestRepo.PokemonResponse, PokePagingItem>(
    viewResource,
    loadingResource,
    pagingLib
) {
    override fun onBindViewHolder(
        holder: GenericPagingAdapterViewHolder<PokePagingItem>,
        position: Int
    ) {
        if(holder is LoadedViewHolder) {
            holder.bindViewModel(items[position] as PokePagingItem)
        }
        super.onBindViewHolder(holder, position)
    }
}

