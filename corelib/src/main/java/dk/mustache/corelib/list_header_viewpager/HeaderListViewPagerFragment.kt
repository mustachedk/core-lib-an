package dk.mustache.corelib.list_header_viewpager

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.FragmentHeaderListViewpagerBinding
import dk.mustache.corelib.utils.getScreenWidth
import dk.mustache.corelib.utils.toPx

class HeaderListViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentHeaderListViewpagerBinding
    private lateinit var horizontalListAdapter: HorizontalListAdapter
    private lateinit var snapHelper: LinearSnapHelper
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var viewListPagerAdapter: BottomPagerStateAdapter
    private var currentTypeListScroll = 0
    private var first = true
    private lateinit var viewModel: HeaderListViewPagerViewModel
    private val scrollingRight = 1
    private val scrollingLeft = -1
    private var scrollDirection = scrollingLeft
    private var isScrollingToStart = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(HeaderListViewPagerViewModel::class.java)
    }

    fun setupViewPager() {
        val pageList = viewModel.pageDataListObservable.get() ?: mutableListOf()
        horizontalListAdapter.updateData(pageList)
        viewListPagerAdapter = BottomPagerStateAdapter(this, ArrayList(pageList ?: ArrayList()))
        binding.headerListPager.offscreenPageLimit = viewModel.settings.get()?.offscreenPageLimit?:1
        binding.headerListPager.adapter = viewListPagerAdapter

        if (viewModel.settings.get()?.compatibilityModePreVersion123==true) {
            binding.horizontalHeaderList.setPadding(0,10.toPx(),0, 10.toPx())
        }

        if (!pageList.isNullOrEmpty()) {
            Handler(Looper.getMainLooper()).post {
                binding.headerListPager.currentItem =
                    viewModel.selectedIndexObservable.get()
            }
            binding.headerListPager.visibility = View.VISIBLE
            viewModel.currentShownPage = viewModel.selectedIndexObservable.get()
            binding.horizontalHeaderList.smoothScrollToPosition(viewModel.currentShownPage)
        } else {
            binding.headerListPager.visibility = View.GONE
        }

        binding.horizontalHeaderList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dx > 0) {
                    horizontalListAdapter.hasScrolled = true
                } else {
                    if (dx < 0) {
                        horizontalListAdapter.hasScrolled = true
                    }
                }
            }
        })

        binding.headerListPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                viewModel.pageScrollStateObservable.set(state)
            }
        })
    }

    fun selectProductGroupByIndex(index: Int) {
        viewModel.currentShownPage = index
        if (index < horizontalListAdapter.itemCount) {
            horizontalListAdapter.selectedIndex = viewModel.currentShownPage

            horizontalListAdapter.notifyDataSetChanged()

            binding.horizontalHeaderList.smoothScrollToPosition(index)
        }
    }

    val selectionListener = object : SelectionListener {

        override fun headerSelected(pageData: PageData<GenericPagerFragment>, index: Int) {
            if (binding.headerListPager.currentItem == index) {
                viewListPagerAdapter.scrollAllToTop()
            }
            if (index == 0) {
                horizontalListAdapter.hasScrolled = false
                horizontalListAdapter.selectedIndex = index
                horizontalListAdapter.notifyDataSetChanged()
            }

            if (index < 1) {
                Handler().postDelayed({
                    if (isAdded)
                        binding.horizontalHeaderList.scrollToPosition(0)
                }, 500)
            }
            binding.headerListPager.currentItem = index
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHeaderListViewpagerBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val settings = viewModel.settings.get()

        horizontalListAdapter = HorizontalListAdapter(
            context = requireActivity(),
            selectionListener = selectionListener,
            selectedIndex = viewModel.currentShownPage,
            settings = settings ?: HeaderListViewPagerSettings(),
            screenWidth = getScreenWidth(requireActivity()),
            listItemLayout = settings?.filterLayoutId ?: R.layout.top_list_item,
            hasScrolled = false,
            padding = settings?.paddingBetweenItems ?: 10,
            paddingStart = settings?.firstItemPaddingStart ?: 100,
            lastItemPaddingEnd = settings?.lastItemPaddingEnd ?: 100
            )
        if (settings?.snapCenter==true) {
            layoutManager = CenterLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(binding.horizontalHeaderList)
        } else {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.horizontalHeaderList.layoutManager = layoutManager
        binding.horizontalHeaderList.adapter = horizontalListAdapter

        binding.headerListPager.isSaveEnabled = false
        binding.headerListPager.reduceDragSensitivity()
        binding.headerListPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (!first) {
                    selectProductGroupByIndex(position)
                    if (position != 0) {
                        val dataList = viewModel.pageDataListObservable.get()?:ArrayList()
                        if (position>=dataList.lastIndex) {
                            Handler(Looper.getMainLooper()).postDelayed({
                                if (isAdded) {
                                    binding.horizontalHeaderList.scrollToPosition(dataList.lastIndex)
                                }
                            },300)
                        }
                        horizontalListAdapter.hasScrolled = true
                    } else {
                        binding.horizontalHeaderList.scrollToPosition(0)
                    }

                    viewModel.selectedIndexObservable.set(position)

                    horizontalListAdapter.notifyItemChanged(0)
                } else {
                    Handler().postDelayed({
                        if (isAdded) {
                            binding.horizontalHeaderList.smoothScrollToPosition(viewModel.currentShownPage)
                        }
                    }, 500)
                    first = false
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        binding.horizontalHeaderList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView!!, dx, dy)

                currentTypeListScroll += dx

                //Scrolling right
                if (dx > 0) {
                    val dataList = viewModel.pageDataListObservable.get()?:ArrayList()
                    if(layoutManager.findLastCompletelyVisibleItemPosition()==dataList.lastIndex) {

                    if (!isScrollingToStart) {
                        isScrollingToStart = true
                            Handler(Looper.getMainLooper()).postDelayed({
                                binding.horizontalHeaderList.scrollToPosition(dataList.lastIndex)
                                isScrollingToStart = false
                                currentTypeListScroll = 0
                            }, 10)
                        }
                    }
                    scrollDirection = scrollingRight
                }
                //Scrolling left
                if (dx < 0) {
                    scrollDirection = scrollingLeft

                    if(layoutManager.findFirstCompletelyVisibleItemPosition()==0){
                        if (!isScrollingToStart) {
                            isScrollingToStart = true
                            Handler(Looper.getMainLooper()).postDelayed({
                                binding.horizontalHeaderList.scrollToPosition(0)
                                isScrollingToStart = false
                                currentTypeListScroll = 0
                            }, 10)
                        }
                    }
                }
            }
        })
        context?.let {
            viewModel.settings.get()?.filterBackgroundColor?.let { it1 ->
                ContextCompat.getColor(it,
                    it1)
            }
        }?.let { binding.horizontalHeaderList.setBackgroundColor(it) }

        setupViewPager()
    }

    override fun onStart() {
        super.onStart()
        viewModel.selectedIndexObservable.addOnPropertyChangedCallback(selectedPageCallback)
        viewModel.pageDataListObservable.addOnPropertyChangedCallback(changeViewPageListCallback)
    }

    override fun onStop() {
        super.onStop()
        viewModel.selectedIndexObservable.removeOnPropertyChangedCallback(selectedPageCallback)
        viewModel.pageDataListObservable.removeOnPropertyChangedCallback(changeViewPageListCallback)
    }


    val selectedPageCallback = object : androidx.databinding.Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            val selectedPage = viewModel.selectedIndexObservable.get()
            binding.headerListPager.currentItem = selectedPage
        }
    }

    val changeViewPageListCallback = object : androidx.databinding.Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            val pageDataList = viewModel.pageDataListObservable.get()
            if (!pageDataList.isNullOrEmpty()) {
                horizontalListAdapter.updateData(pageDataList.map { it })
                if (!pageDataList.isNullOrEmpty()) {
                    viewListPagerAdapter.replaceData(pageDataList)
                }
            } else {

            }
        }
    }

    /**
     * Reduces drag sensitivity of [ViewPager2] widget
     */
    fun ViewPager2.reduceDragSensitivity() {
        val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView = recyclerViewField.get(this) as RecyclerView

        val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
        val touchSlop = touchSlopField.get(recyclerView) as Int
        touchSlopField.set(recyclerView, touchSlop * Math.abs(viewModel.settings.get()?.swipeSensitivity?:6))
    }

    @SuppressLint("UseRequireInsteadOfGet")
    inner class BottomPagerStateAdapter(
        val fragment: Fragment,
        var pagerDataList: ArrayList<PageData<GenericPagerFragment>>
    ) : FragmentStateAdapter(fragment.childFragmentManager!!, fragment.lifecycle!!) {

        private var fragmentList = ArrayList<GenericPagerFragment>()

        fun clearAdapter() {
            pagerDataList.clear()
            fragmentList.clear()
            notifyDataSetChanged()
        }

        fun scrollAllToTop() {
            fragmentList.forEach {
                it.scrollToTop()
            }
        }

        fun removeAllData(dataChangedCallback: () -> Unit) {
            pagerDataList.clear()
            fragmentList.clear()
            notifyDataSetChanged()
        }

        fun replaceData(newPagerDataList: List<PageData<GenericPagerFragment>>) {
            pagerDataList.clear()
            fragmentList.clear()
            pagerDataList.addAll(newPagerDataList)
            notifyDataSetChanged()
        }

        fun updateAll() {
            fragmentList.forEach {
                it.update()
            }
        }

        fun addPageData(pog: PageData<GenericPagerFragment>) {
            pagerDataList.add(pog)
            notifyDataSetChanged()
        }

        override fun createFragment(position: Int): Fragment {
            val newFragment = pagerDataList[position].clazz.newInstance()
            val args = Bundle().apply {
                putSerializable(GenericPagerFragment.PAGE_DATA, pagerDataList[position])
            }

            newFragment.arguments = args
            fragmentList.add(newFragment)
            return newFragment
        }

        override fun getItemId(position: Int): Long {
            return pagerDataList[position].topListItemText.hashCode().toLong()
        }

        override fun getItemCount(): Int {
            return pagerDataList.size
        }

    }

    companion object {
        const val PAGE_LIST = "page_list"
        const val SETTINGS = "header_list_viewpager_settings"
        const val FRAGMENT_TYPE = "fragment_type"

        fun newInstance(): HeaderListViewPagerFragment {
            val args = Bundle()

            val fragment = HeaderListViewPagerFragment()
            fragment.arguments = args
            return fragment
        }
    }

}