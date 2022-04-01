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
import dk.mustache.corelib.syncviews.*
import dk.mustache.corelibexample.R

class SyncViewsFragment2: Fragment() {
    private val conductor = PositionSyncConductor()
    private lateinit var titleSyncHandler: TitleSyncHandler
    private lateinit var buttonsSyncHandler: ButtonsViewSync
    private lateinit var viewPagerHandler: ViewPagerSyncHandler
    private lateinit var listHandler: RecyclerViewSyncHandler

    private lateinit var pageAdapter: SyncedViewPagerAdapter
    private lateinit var listAdapter: SyncedSelectableListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_sync_views, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        pageAdapter = SyncedViewPagerAdapter(requireActivity(), 6)
        viewPager.adapter = pageAdapter

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)
        val syncViewsParent = findViewById<SyncViewsParentFrameLayout>(R.id.sync_views_parent)

        listAdapter = SyncedSelectableListAdapter(
            listOf(
                Item(0, true),
                Item(1, false),
                Item(2, false),
                Item(3, false),
                Item(4, false),
                Item(5, false)
            ),
            {
                    item, selected -> if(selected) { syncViewsParent.sendMessage(
                SyncViewEvent(item.index)
            ) }
            },
            SelectableAdapterSettings(singleSelection = true, layoutResId = R.layout.item_tall)
        )
        recyclerview.layoutManager = LinearLayoutManager(requireActivity())
        recyclerview.adapter = listAdapter
    }

    private fun <T:View?> findViewById(@IdRes id: Int): T {
        return requireView().findViewById(id)
    }
}