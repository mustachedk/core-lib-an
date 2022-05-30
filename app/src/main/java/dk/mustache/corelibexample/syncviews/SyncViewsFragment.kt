package dk.mustache.corelibexample.syncviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import dk.mustache.corelib.selectable_list.SelectableAdapterSettings
import dk.mustache.corelib.syncviews.PositionSyncConductor
import dk.mustache.corelib.syncviews.RecyclerViewSyncHandler
import dk.mustache.corelib.syncviews.ViewPagerSyncHandler
import dk.mustache.corelibexample.R
import dk.mustache.corelibexample.databinding.FragmentSyncviewsBinding

class SyncViewsFragment : Fragment() {
    private lateinit var binding: FragmentSyncviewsBinding

    private val conductor = PositionSyncConductor()
    private lateinit var titleSyncHandler: TitleSyncHandler
    private lateinit var buttonsSyncHandler: ButtonsSyncHandler
    private lateinit var viewPagerHandler: ViewPagerSyncHandler
    private lateinit var listHandler: RecyclerViewSyncHandler

    private lateinit var pageAdapter: SyncedViewPagerAdapter
    private lateinit var listAdapter: SyncedSelectableListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSyncviewsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = binding.viewPager
        pageAdapter = SyncedViewPagerAdapter(requireActivity(), 6)
        viewPager.adapter = pageAdapter

        val recyclerview = binding.recyclerView

        titleSyncHandler = TitleSyncHandler(binding.txtTitle, conductor)
        buttonsSyncHandler = ButtonsSyncHandler(
            binding.btn1,
            binding.btn2,
            binding.btn3,
            binding.btn4,
            binding.btn5,
            binding.btn6,
            conductor
        )
        viewPagerHandler = ViewPagerSyncHandler(viewPager, conductor)
        listHandler = RecyclerViewSyncHandler(recyclerview, conductor)

        listAdapter = SyncedSelectableListAdapter(
            listOf(
                Item(0, true),
                Item(1, false),
                Item(2, false),
                Item(3, false),
                Item(4, false),
                Item(5, false)
            ),
            { item, selected ->
                if (selected) {
                    listHandler.onItemSelected(item.index)
                }
            },
            SelectableAdapterSettings(true, R.layout.item_tall)
        )
        recyclerview.layoutManager = LinearLayoutManager(requireActivity())
        recyclerview.adapter = listAdapter
    }
}