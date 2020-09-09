package dk.mustache.corelib.list_header_viewpager

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import dk.mustache.corelib.databinding.FragmentOfferMainBinding

class HeaderListViewpagerFragment : Fragment() {

    private lateinit var binding: FragmentOfferMainBinding
    private lateinit var productGroupAdapter: ProductGroupItemAdapter
    private lateinit var snapHelper: LinearSnapHelper
    private lateinit var layoutManager: CenterLayoutManager
    private lateinit var offerListPagerAdapter: OffersPagerAdapter
    private var currentTypeListScroll = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productGroupAdapter = ProductGroupItemAdapter(requireActivity(), selectionListener, OfferMainManager.selectedProductGroupIndexFilter)
        layoutManager = CenterLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.offerTypeList.layoutManager = layoutManager
        binding.offerTypeList.adapter = productGroupAdapter

        snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.offerTypeList)

        binding.offerListPager.isSaveEnabled = false
        binding.offerListPager.reduceDragSensitivity()
        binding.offerListPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (!first) {
                    selectProductGroupByIndex(position)
                    animateMenuIn()
                    listener?.showMainNavigationTabs(true)
                    if (position!=0)
                        productGroupAdapter.hasScrolled = true
                    productGroupAdapter.notifyItemChanged(0)
                } else {
                    Handler().postDelayed({
                        if(isAdded) {
                            binding.offerTypeList.smoothScrollToPosition(OfferMainManager.selectedProductGroupIndexFilter)
//                            offerListPagerAdapter.notifyItemChanged(OfferMainManager.selectedProductGroupIndexFilter)
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

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentOfferMainBinding.inflate(inflater, container, false)

        return binding.root
    }


    @SuppressLint("UseRequireInsteadOfGet")
    inner class OffersPagerAdapter(val fragment: Fragment, var productOfferList: ArrayList<PageData>) : FragmentStateAdapter(fragment.childFragmentManager!!, fragment.lifecycle!!) {

        private var fragmentList = ArrayList<OfferListFragment>()

        fun clearAdapter() {
            productOfferList.clear()
            fragmentList.clear()
            notifyDataSetChanged()
        }

        fun scrollAllToTop() {
            fragmentList.forEach {
                it.scrollToTop()
            }
        }

        fun updateAll() {
            fragmentList.forEach {
                it.update()
            }
        }

        fun addOfferList(pog: ProductOfferGroup) {
            productOfferList.add(pog)
            notifyDataSetChanged()
        }

        override fun createFragment(position: Int): Fragment {
            val newFragment = OfferListFragment.newInstance(ArrayList(), "", productOfferList[position].productGroup, position)
            fragmentList.add(newFragment)
            return newFragment
        }

        override fun getItemCount(): Int {
            return productOfferList.size
        }

    }

}