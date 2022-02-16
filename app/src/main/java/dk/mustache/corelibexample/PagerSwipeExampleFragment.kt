package dk.mustache.corelibexample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import dk.mustache.corelib.MustacheCoreLib
import dk.mustache.corelib.list_header_viewpager.GenericPagerFragment
import dk.mustache.corelib.list_header_viewpager.HeaderListViewPagerViewModel
import dk.mustache.corelib.selectable_list.SelectableItem
import dk.mustache.corelib.sticky_header_decoration.StickyHeaderItemDecoration
import dk.mustache.corelib.swipe_recyclerview_item.LockableLayoutManager
import dk.mustache.corelib.swipe_recyclerview_item.SwipeSettingsEnum
import dk.mustache.corelibexample.databinding.FragmentListHeaderPagerBinding
import dk.mustache.corelibexample.section_header_example.SectionHeaderExampleAdapter
import dk.mustache.corelibexample.section_header_example.SectionExampleItem
import dk.mustache.corelibexample.section_header_example.SectionHeaderExampleViewModel
import dk.mustache.corelibexample.selectable_list_example.SelectableExampleAdapter
import dk.mustache.corelibexample.selectable_list_example.SelectableExampleItem
import dk.mustache.corelibexample.swipe_recyclerview_item_example.SwipeListAdapter
import dk.mustache.corelibexample.swipe_recyclerview_item_example.SwipeSectionExampleItem

open class PagerSwipeExampleFragment : Fragment() {

    private lateinit var headerListViewPagerViewModel: HeaderListViewPagerViewModel
    lateinit var binding: FragmentListHeaderPagerBinding
    lateinit var viewModel: SectionHeaderExampleViewModel
    private var totalScroll: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentListHeaderPagerBinding.inflate(inflater, container, false)
//        binding.pageData = pageData as SpecialData

        val item1 = SwipeSectionExampleItem("test7", 0, false, 1)
        val item2 = SwipeSectionExampleItem("test1", 1, false, 2)
        val item3 = SwipeSectionExampleItem("test1",1, false, 3)
        val item4 = SwipeSectionExampleItem("test2", 2, false, 4)
        val item11 = SwipeSectionExampleItem( "test4",3, false, 5)
        val item12 = SwipeSectionExampleItem("test4",3, false, 6)
        val item13 = SwipeSectionExampleItem("test4", 3, false, 7)
        val item14 = SwipeSectionExampleItem("test5", 4, false, 8)
        val item15 = SwipeSectionExampleItem("test7", 0, false, 9)
        val item16 = SwipeSectionExampleItem("test1", 1, false, 10)
        val item17 = SwipeSectionExampleItem("test1",1, false, 11)
        val item18 = SwipeSectionExampleItem("test2", 2, false, 12)
        val item19 = SwipeSectionExampleItem( "test4",3, false, 13)
        val item20 = SwipeSectionExampleItem("test4",3, false, 14)
        val item21 = SwipeSectionExampleItem("test4", 3, false, 15)
        val item22 = SwipeSectionExampleItem("test5", 4, false, 16)
        val item23 = SwipeSectionExampleItem("test1", 1, false, 17)
        val item24 = SwipeSectionExampleItem("test1",1, false, 18)
        val item25 = SwipeSectionExampleItem("test2", 2, false, 19)
        val item26 = SwipeSectionExampleItem( "test4",3, false, 20)
        val item27 = SwipeSectionExampleItem("test4",3, false, 21)
        val item28 = SwipeSectionExampleItem("test4", 3, false, 22)
        val item29 = SwipeSectionExampleItem("test5", 4, false, 23)
        val item30 = SwipeSectionExampleItem("test7", 0, false, 24)
        val item31 = SwipeSectionExampleItem("test1", 1, false, 25)
        val item32 = SwipeSectionExampleItem("test1",1, false, 26)
        val item33 = SwipeSectionExampleItem("test2", 2, false, 27)
        val item34 = SwipeSectionExampleItem( "test4",3, false, 28)
        val item35 = SwipeSectionExampleItem("test4",3, false, 29)
        val item36 = SwipeSectionExampleItem("test4", 3, false, 30)
        val item37 = SwipeSectionExampleItem("test5", 4, false, 31)
        val list = ArrayList(listOf(item1, item2, item3, item4, item11, item12, item13, item14, item15, item16, item17, item18, item19, item20, item21, item22, item23, item24, item25, item26, item27, item28, item29, item30, item31, item32, item33, item34, item35, item36, item37))

        val viewModel2 = ViewModelProvider(this).get(SectionHeaderExampleViewModel::class.java)
//        viewModel2.listWithSectionHeaders = list
//
//        headerListViewPagerViewModel = ViewModelProvider(requireActivity()).get(
//            HeaderListViewPagerViewModel::class.java)
//        binding.offerTypeList.addOnScrollListener(object : RecyclerView.OnScrollListener(){
//
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                totalScroll += dy
//                headerListViewPagerViewModel.settings.get()?.setTopListTranslationY(totalScroll)
//            }
//        })
//
//
        val adapter = SwipeListAdapter(list, viewModel2, SwipeSettingsEnum.BOTH, R.layout.swipe_section_row_item, R.layout.swipe_section_row_item, R.layout.section_header_item, {
            //onClick
        }, { swipeLayout, swipeDirection, swipeItem ->
            //swiped
        }, { swipeLayout, swipeDirection, swipeItem ->
            //is swiping
        }) { doLockScroll ->
            lockScroll(doLockScroll)
        }

        val stickyDecoration = StickyHeaderItemDecoration(binding.offerTypeList, R.layout.section_header_item, adapter)
        binding.offerTypeList.addItemDecoration(stickyDecoration)

        (binding.offerTypeList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        binding.offerTypeList.adapter = adapter
        binding.offerTypeList.layoutManager = LockableLayoutManager(MustacheCoreLib.getContextCheckInit())

//        BarcodeBitmapCreator.createBarcodeBitmapFromString("9781782808084", BarcodeFormat.EAN_13, 300.toPx(), 160.toPx()) {
//              binding.barcodeLayout.setImageBitmap(it)
//        }

        adapter.updateDataAndAddHeaders(list)
        return binding.root
    }

    fun lockScroll(locked: Boolean) {
        val layoutManager = binding.offerTypeList.layoutManager as LockableLayoutManager
        layoutManager.setScrollEnabled(!locked)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}