package dk.mustache.corelib.adapters

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.BR

class DataBindingViewHolder<T, U>(val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T, viewModel: U) {
        binding.setVariable(BR.viewModel, viewModel)
        binding.setVariable(BR.item, item)
        binding.executePendingBindings()
    }
}
