package dk.mustache.corelib.list_header_viewpager

import androidx.recyclerview.widget.DiffUtil

class OfferTypeDiffCallback : DiffUtil.ItemCallback<ProductGroup>() {

    override fun areItemsTheSame(
        oldItem: ProductGroup,
        newItem: ProductGroup
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: ProductGroup,
        newItem: ProductGroup
    ): Boolean {
        return oldItem == newItem
    }
}