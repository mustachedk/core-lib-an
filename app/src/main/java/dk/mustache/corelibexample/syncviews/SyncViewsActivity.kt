package dk.mustache.corelibexample.syncviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import dk.mustache.corelib.selectable_list.SelectableAdapterSettings
import dk.mustache.corelib.syncviews.PositionSyncConductor
import dk.mustache.corelib.syncviews.RecyclerViewSyncHandler
import dk.mustache.corelib.syncviews.SyncConductor
import dk.mustache.corelib.syncviews.ViewPagerSyncHandler
import dk.mustache.corelibexample.R

class SyncViewsActivity : AppCompatActivity() {
    private val conductor = PositionSyncConductor()
    lateinit var titleSyncHandler: TitleSyncHandler
    lateinit var buttonsSyncHandler: ButtonsSyncHandler
    lateinit var viewPagerHandler: ViewPagerSyncHandler
    lateinit var listHandler: RecyclerViewSyncHandler

    val pageAdapter = SyncedViewPagerAdapter(this, 6)
    lateinit var listAdapter: SyncedSelectableListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_syncviews)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = pageAdapter

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)

        titleSyncHandler = TitleSyncHandler(findViewById(R.id.txtTitle), conductor)
        buttonsSyncHandler = ButtonsSyncHandler(
            findViewById(R.id.btn1),
            findViewById(R.id.btn2),
            findViewById(R.id.btn3),
            findViewById(R.id.btn4),
            findViewById(R.id.btn5),
            findViewById(R.id.btn6),
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
            {item, selected -> if(selected) {listHandler.onItemSelected(item.index) }},
            SelectableAdapterSettings(true, R.layout.item_tall)
        )
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = listAdapter
    }
}