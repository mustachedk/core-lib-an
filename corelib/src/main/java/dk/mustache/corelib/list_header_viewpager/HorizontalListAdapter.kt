package dk.mustache.corelib.list_header_viewpager

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.BR
import dk.mustache.corelib.R


@Suppress("UNCHECKED_CAST")
class HorizontalListAdapter (
    val context: Context,
    private val selectionListener: SelectionListener,
    var selectedIndex: Int,
    val settings: HeaderListViewPagerSettings,
    val screenWidth: Int,
    val listItemLayout: Int = R.layout.top_list_item,
    var hasScrolled: Boolean = false,
    val padding: Int = 10,
    val paddingStart: Int = 0,
    val lastItemPaddingEnd: Int = 100,
    private val items: MutableList<PageData<GenericPagerFragment>> = mutableListOf()
) : RecyclerView.Adapter<HorizontalListAdapter.HorizontalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalViewHolder {

        val viewHolder = HorizontalViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    listItemLayout, parent, false
                )
        )

        return viewHolder
    }

    fun setWidthAndHeightOfLayout(viewHolder: RecyclerView.ViewHolder) {
        val layoutParams = viewHolder.itemView.layoutParams
        val fixedHeight = settings.horizontalListHeight
        when (settings.type) {
            HeaderListViewPagerTypeEnum.STRETCH -> {
                var width: Int =  (screenWidth / itemCount)
                val usedLayoutParams = if (layoutParams!=null) {
                    layoutParams.width = width
                    if (fixedHeight>0) {
                        layoutParams.height = fixedHeight
                    }
                    layoutParams
                } else {
                    RecyclerView.LayoutParams(width, fixedHeight)
                }
                viewHolder.itemView.layoutParams = usedLayoutParams
            }
            else -> {
                if (fixedHeight>0) {
                    layoutParams.height = fixedHeight
                    viewHolder.itemView.layoutParams = layoutParams
                }
            }
        }
    }

    override fun onBindViewHolder(holder: HorizontalViewHolder, position: Int) {
        val data = items[position]
        data.let { pageData ->
            with(holder) {

                val halfPaddingBetween = padding/2
                if (position>0) {
                    if (position == itemCount - 1) {
                        bind(pageData, selectionListener, position, selectedIndex)
                        holder.itemView.setPadding(halfPaddingBetween, 0, lastItemPaddingEnd, 0)
                        setWidthAndHeightOfLayout(holder)
                    } else {
                        bind(pageData, selectionListener, position, selectedIndex)
                        setWidthAndHeightOfLayout(holder)
                        holder.itemView.setPadding(halfPaddingBetween, 0, halfPaddingBetween, 0)
                    }
                } else {
                    if (itemCount>1) {
                        bind(pageData, selectionListener, position, selectedIndex)
                        holder.itemView.setPadding(paddingStart, 0, halfPaddingBetween, 0)
                        setWidthAndHeightOfLayout(holder)
                    } else {
                        bind(pageData, selectionListener, position, selectedIndex)
                        holder.itemView.setPadding(paddingStart, 0, lastItemPaddingEnd, 0)
                        setWidthAndHeightOfLayout(holder)
                    }
                }

                itemView.tag = pageData
            }
        }
    }

    class HorizontalViewHolder(
        val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        lateinit var viewModel: HorizontalListItemViewModel

        fun bind(
            pageData: PageData<GenericPagerFragment>,
            selectionListener: SelectionListener?,
            index: Int,
            selectedIndex: Int
        ) {

            viewModel = HorizontalListItemViewModel(
                pageData, selectionListener, index, selectedIndex
            )
            binding.setVariable(BR.viewModel, viewModel)
            binding.executePendingBindings()
        }
    }

    fun updateData(newItems: List<PageData<GenericPagerFragment>>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

interface SelectionListener {
    fun headerSelected(pageData: PageData<GenericPagerFragment>, index: Int)
}
