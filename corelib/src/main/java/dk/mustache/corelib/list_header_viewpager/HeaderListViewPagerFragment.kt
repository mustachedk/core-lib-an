package dk.mustache.corelib.list_header_viewpager

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import dk.mustache.corelib.databinding.FragmentHeaderListViewpagerBinding
import dk.mustache.corelib.utils.getScreenWidth

class HeaderListViewPagerFragment <T : GenericPagerFragment<U>, U : PageData> () : Fragment() {

    private lateinit var binding: FragmentHeaderListViewpagerBinding
    private lateinit var horizontalListAdapter: HorizontalListAdapter
    private lateinit var snapHelper: LinearSnapHelper
    private lateinit var layoutManager: CenterLayoutManager
    private lateinit var offerListPagerAdapter: BottomPagerAdapter<T, U>
    private var currentTypeListScroll = 0
    private var first = true
    private lateinit var viewModel: HeaderListViewPagerViewModel
    private val scrollingRight = 1
    private val scrollingLeft = -1
    private var scrollDirection = scrollingLeft
    private var isScrollingToStart = false
    private lateinit var pageList: PageList<PageData>
    private lateinit var settings: HeaderListViewPagerSettings
    private lateinit var clazz: Class<T>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(HeaderListViewPagerViewModel::class.java)

    }

    fun setupViewPager() {
        horizontalListAdapter.submitList(pageList.pageList)
        offerListPagerAdapter = BottomPagerAdapter(this, ArrayList(pageList.pageList))
        binding.offerListPager.offscreenPageLimit = 1
        binding.offerListPager.adapter = offerListPagerAdapter

        if (pageList.pageList.isNotEmpty()) {
            Handler().postDelayed({
                binding.offerListPager.currentItem =
                    viewModel.selectedIndex
            }, 100)
            binding.offerListPager.visibility = View.VISIBLE
            binding.offerTypeList.smoothScrollToPosition(viewModel.selectedIndex)
        } else {
            binding.offerListPager.visibility = View.GONE
        }

        binding.offerTypeList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dx > 0) {
                    horizontalListAdapter.hasScrolled = true
                } else {
                    if (dx<0) {
                        horizontalListAdapter.hasScrolled = true
                    }
                }
            }
        })
    }

    fun selectProductGroupByIndex(index: Int) {
        viewModel.selectedIndex = index
        if (index<horizontalListAdapter.currentList.size) {
            horizontalListAdapter.selectedIndex = viewModel.selectedIndex

            horizontalListAdapter.notifyDataSetChanged()

            binding.offerTypeList.smoothScrollToPosition(index)
        }
    }

    val selectionListener = object: ProductGroupSelectionListener {
        override fun typeSelected(type: PageData, index: Int) {
            if (binding.offerListPager.currentItem==index) {
                offerListPagerAdapter.scrollAllToTop()
            }
            if (index==0) {
                horizontalListAdapter.hasScrolled = false
                horizontalListAdapter.selectedIndex = index
                horizontalListAdapter.notifyDataSetChanged()
            }

            if (index<2) {
                Handler().postDelayed({
                    if (isAdded)
                        binding.offerTypeList.scrollToPosition(0)
                }, 500)
            }
            binding.offerListPager.currentItem = index
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentHeaderListViewpagerBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        pageList = arguments?.getParcelable<PageList<PageData>?>(PAGE_LIST)?:PageList(ArrayList())
        settings = arguments?.getParcelable(SETTINGS)?:HeaderListViewPagerSettings()
        clazz = arguments?.getSerializable(FRAGMENT_TYPE) as Class<T>

        horizontalListAdapter = HorizontalListAdapter(requireActivity(), selectionListener, viewModel.selectedIndex, settings, getScreenWidth(requireActivity()), settings.topListLayoutId)
        layoutManager = CenterLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.offerTypeList.layoutManager = layoutManager
        binding.offerTypeList.adapter = horizontalListAdapter

        snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.offerTypeList)

        binding.offerListPager.isSaveEnabled = false
        binding.offerListPager.reduceDragSensitivity()
        binding.offerListPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (!first) {
                    selectProductGroupByIndex(position)
                    if (position!=0)
                        horizontalListAdapter.hasScrolled = true
                    horizontalListAdapter.notifyItemChanged(0)
                } else {
                    Handler().postDelayed({
                        if(isAdded) {
                            binding.offerTypeList.smoothScrollToPosition(viewModel.selectedIndex)
                        }
                    }, 500)
                    first = false
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        binding.offerTypeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView!!, dx, dy)

                currentTypeListScroll += dx

                //Scrolling right
                if (dx>0) {
                    scrollDirection = scrollingRight
                }
                //Scrolling left
                if (dx<0) {
                    scrollDirection = scrollingLeft
                    if (currentTypeListScroll<100) {
                        if (!isScrollingToStart) {
                            isScrollingToStart = true
                            Handler().postDelayed({
                                if (isAdded)
                                    binding.offerTypeList.scrollToPosition(0)
                                currentTypeListScroll = 0
                                isScrollingToStart = false
                            }, 500)
                        }
                    }
                }
            }
        })

        setupViewPager()
    }

    override fun onStart() {
        super.onStart()
        viewModel.pageDataListObservable.addOnPropertyChangedCallback(callBack)
    }

    override fun onStop() {
        super.onStop()
        viewModel.pageDataListObservable.removeOnPropertyChangedCallback(callBack)
    }

    val callBack = object : androidx.databinding.Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            val pageDataList = viewModel.pageDataListObservable.get()
            if (!pageDataList.isNullOrEmpty()) {
                horizontalListAdapter.submitList(pageDataList)
                horizontalListAdapter.notifyDataSetChanged()
                offerListPagerAdapter.updateData(pageDataList)
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
        touchSlopField.set(recyclerView, touchSlop*6)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    inner class BottomPagerAdapter<T : GenericPagerFragment<U>, U : PageData>(val fragment: Fragment, var pagerDataList: ArrayList<PageData>) : FragmentStateAdapter(fragment.childFragmentManager!!, fragment.lifecycle!!) {

        private var fragmentList = ArrayList<T>()
        private val handler = Handler()

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

        fun updateData(newPagerDataList: List<PageData>) {
            pagerDataList.clear()
            fragmentList.clear()
            notifyDataSetChanged()
            //TODO investigate if a better solution exists
            //If fragment destruction takes too long, the data will not be updated
            handler.postDelayed({
                if(isAdded) {
                    pagerDataList.addAll(newPagerDataList)
                    notifyItemRangeChanged(0, newPagerDataList.lastIndex)
                }
            },1000)
        }

        fun updateAll() {
            fragmentList.forEach {
                it.update()
            }
        }

        fun addPageData(pog: PageData) {
            pagerDataList.add(pog)
            notifyDataSetChanged()
        }

        override fun createFragment(position: Int): Fragment {
            val newFragment = clazz.newInstance() as T
            val args = Bundle().apply {
                putParcelable(GenericPagerFragment.PAGE_DATA, pagerDataList[position])
            }

            newFragment.arguments = args
            fragmentList.add(newFragment)
            return newFragment
        }

        override fun getItemCount(): Int {
            return pagerDataList.size
        }

    }

    companion object {
        const val PAGE_LIST = "page_list"
        const val SETTINGS = "header_list_viewpager_settings"
        const val FRAGMENT_TYPE = "fragment_type"

        fun <T:GenericPagerFragment<U>, U: PageData> newInstance(pageList: PageList<U>, clazz: Class<T>, settings: HeaderListViewPagerSettings = HeaderListViewPagerSettings()): HeaderListViewPagerFragment<T, U> {
            val args = Bundle()
            args.putParcelable(PAGE_LIST, pageList)
            args.putParcelable(SETTINGS, settings)
            args.putSerializable(FRAGMENT_TYPE, clazz)

            val fragment = HeaderListViewPagerFragment<T, U>()
            fragment.arguments = args
            return fragment
        }
    }

}