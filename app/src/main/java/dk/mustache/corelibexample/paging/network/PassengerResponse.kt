package dk.test.pagingadaptertest.network

import dk.mustache.corelib.paging.GenericPagingAdapter

data class PassengersResponse(
    val totalPassengers: Long,
    val totalPages: Long,
    val data: List<Passenger>
)

data class Passenger(
    val id: Long? = null,
    val name: String? = null,
    val country: String? = null,
    val logo: String? = null,
    val slogan: String? = null,
    val head_quaters: String? = null,
    val website: String? = null,
    val established: String? = null,
    var page: Int? = null
): GenericPagingAdapter.PagingAdapterItem()