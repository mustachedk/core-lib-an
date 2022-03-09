package dk.mustache.corelibexample.paging

import dk.mustache.corelib.paging.GenericPagingAdapter
import dk.mustache.corelib.paging.Paging
import dk.mustache.corelibexample.testdata.PokemonTestRepo
import dk.mustache.corelibexample.testdata.PokemonTestRepo.PokemonResponse

class PagingAdapter(
    viewResource: Int,
    loadingResource: Int,
    pagingLib: Paging<PokemonPagingItem, PokemonResponse>
) : GenericPagingAdapter<PokemonResponse, PokemonPagingItem>(
    viewResource,
    loadingResource,
    pagingLib
) {

    init {
        pagingLib.setMapper(::mapper)
    }

    override fun onBindViewHolder(
        holder: GenericPagingAdapterViewHolder<PokemonPagingItem>,
        position: Int
    ) {
        if(holder is LoadedViewHolder) {
            holder.bindViewModel(items[position] as PokemonPagingItem)
        }
        super.onBindViewHolder(holder, position)
    }

    private fun mapper(response: PokemonResponse): List<PokemonPagingItem> {
        return response.data.map { PokemonPagingItem(it.id, it.name, it.attack, it.defense) }
    }
}

data class PokemonPagingItem(val id: Int, val name: String, val attack: Int, val defense: Int) :
    GenericPagingAdapter.PagingAdapterItem() {

    val text = "$id: $name (att: $attack, def: $defense)"
}

