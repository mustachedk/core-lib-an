package dk.mustache.corelib.list_header_viewpager

import androidx.recyclerview.widget.DiffUtil

class OfferTypeDiffCallback : DiffUtil.ItemCallback<PageData<GenericPagerFragment>>() {

    override fun areItemsTheSame(
        oldItem: PageData<GenericPagerFragment>,
        newItem: PageData<GenericPagerFragment>
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: PageData<GenericPagerFragment>,
        newItem: PageData<GenericPagerFragment>
    ): Boolean {
        return oldItem == newItem
    }
}