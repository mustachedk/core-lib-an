package dk.mustache.corelibexample.paging

import dk.mustache.corelib.paging.GenericPagingAdapter
import dk.mustache.corelibexample.testdata.PokemonTestRepo

data class PokePagingItem(val id: Int, val name: String, val attack: Int, val defense: Int) :
    GenericPagingAdapter.PagingAdapterItem() {
    companion object {
        fun map(orig: PokemonTestRepo.PokemonDTO): PokePagingItem {
            return PokePagingItem(orig.id, orig.name, orig.attack, orig.defense)
        }
    }
}