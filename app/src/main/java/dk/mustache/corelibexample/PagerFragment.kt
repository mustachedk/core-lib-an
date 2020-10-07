package dk.mustache.corelibexample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import dk.mustache.corelib.list_header_viewpager.GenericPagerFragment
import dk.mustache.corelib.list_header_viewpager.HeaderListViewPagerViewModel
import dk.mustache.corelibexample.databinding.FragmentListHeaderPagerBinding
import dk.mustache.corelibexample.section_header_example.SectionHeaderExampleAdapter
import dk.mustache.corelibexample.section_header_example.SectionExampleItem
import dk.mustache.corelibexample.section_header_example.SectionHeaderExampleViewModel

open class PagerFragment : GenericPagerFragment() {

    private lateinit var headerListViewPagerViewModel: HeaderListViewPagerViewModel
    lateinit var binding: FragmentListHeaderPagerBinding
    lateinit var viewModel: SectionHeaderExampleViewModel
    private var totalScroll: Int = 0
    override fun update() {

    }

    override fun scrollToTop() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentListHeaderPagerBinding.inflate(inflater, container, false)
        binding.pageData = pageData as SpecialData

        val item1 = SectionExampleItem("test7", 0)
        val item2 = SectionExampleItem("test1", 1)
        val item3 = SectionExampleItem("test1",1)
        val item4 = SectionExampleItem("test2", 2)
        val item11 = SectionExampleItem( "test4",3)
        val item12 = SectionExampleItem("test4",3)
        val item13 = SectionExampleItem("test4", 3)
        val item14 = SectionExampleItem("test5", 4)
        val item15 = SectionExampleItem("test7", 0)
        val item16 = SectionExampleItem("test1", 1)
        val item17 = SectionExampleItem("test1",1)
        val item18 = SectionExampleItem("test2", 2)
        val item19 = SectionExampleItem( "test4",3)
        val item20 = SectionExampleItem("test4",3)
        val item21 = SectionExampleItem("test4", 3)
        val item22 = SectionExampleItem("test5", 4)
        val item23 = SectionExampleItem("test1", 1)
        val item24 = SectionExampleItem("test1",1)
        val item25 = SectionExampleItem("test2", 2)
        val item26 = SectionExampleItem( "test4",3)
        val item27 = SectionExampleItem("test4",3)
        val item28 = SectionExampleItem("test4", 3)
        val item29 = SectionExampleItem("test5", 4)
        val item30 = SectionExampleItem("test7", 0)
        val item31 = SectionExampleItem("test1", 1)
        val item32 = SectionExampleItem("test1",1)
        val item33 = SectionExampleItem("test2", 2)
        val item34 = SectionExampleItem( "test4",3)
        val item35 = SectionExampleItem("test4",3)
        val item36 = SectionExampleItem("test4", 3)
        val item37 = SectionExampleItem("test5", 4)
        val list = ArrayList(listOf(item1, item2, item3, item4, item11, item12, item13, item14, item15, item16, item17, item18, item19, item20, item21, item22, item23, item24, item25, item26, item27, item28, item29, item30, item31, item32, item33, item34, item35, item36, item37))

        viewModel = ViewModelProvider(this).get(SectionHeaderExampleViewModel::class.java)
        viewModel.listWithSectionHeaders = list

        headerListViewPagerViewModel = ViewModelProvider(requireActivity()).get(
            HeaderListViewPagerViewModel::class.java)
        binding.offerTypeList.addOnScrollListener(object : RecyclerView.OnScrollListener(){


            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalScroll += dy
                headerListViewPagerViewModel.settings.get()?.setTopListTranslationY(totalScroll)
            }
        })


        val adapter = SectionHeaderExampleAdapter(viewModel) {

        }
        binding.offerTypeList.adapter = adapter

        adapter.updateDataAndAddHeaders(ArrayList(viewModel.listWithSectionHeaders?: listOf()))

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}