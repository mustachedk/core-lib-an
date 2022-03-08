package dk.mustache.corelibexample.paging

import dk.mustache.corelib.paging.GenericPagingAdapter
import dk.test.pagingadaptertest.network.Passenger

class PagingAdapter(viewResource: Int, loadingResource: Int) :
    GenericPagingAdapter<Passenger>(viewResource, loadingResource) {

    override fun onBindViewHolder(
        holder: GenericPagingAdapterViewHolder<Passenger>,
        position: Int
    ) {
        if (items[position] is Passenger) {
            holder.bindViewModel(items[position] as Passenger)
        }
        super.onBindViewHolder(holder, position)
    }
}

