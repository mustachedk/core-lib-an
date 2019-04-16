package dk.mustache.corelib.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class DataBindingAdapter<T, U>(diffCallback: DiffUtil.ItemCallback<T>, private val viewModel: U) :
    ListAdapter<T, DataBindingViewHolder<T, U>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T, U> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T, U>, position: Int) = holder.bind(getItem(position), viewModel)

    abstract override fun getItemViewType(position: Int): Int
}
