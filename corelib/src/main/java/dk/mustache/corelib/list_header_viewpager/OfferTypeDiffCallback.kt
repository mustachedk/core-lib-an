package dk.mustache.corelib.list_header_viewpager

import androidx.recyclerview.widget.DiffUtil

class OfferTypeDiffCallback : DiffUtil.ItemCallback<PageData>() {

    override fun areItemsTheSame(
        oldItem: PageData,
        newItem: PageData
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: PageData,
        newItem: PageData
    ): Boolean {
        return oldItem == newItem
    }
}