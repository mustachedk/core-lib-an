package dk.mustache.corelibexample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import dk.mustache.corelib.list_header_viewpager.GenericPagerFragment
import dk.mustache.corelibexample.databinding.FragmentListHeaderPagerBinding
import dk.mustache.corelibexample.section_header_example.SectionHeaderExampleAdapter
import dk.mustache.corelibexample.section_header_example.SectionExampleItem
import dk.mustache.corelibexample.section_header_example.SectionHeaderExampleViewModel

open class PagerFragment : GenericPagerFragment() {

    lateinit var binding: FragmentListHeaderPagerBinding
    lateinit var viewModel: SectionHeaderExampleViewModel

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
        val list = ArrayList(listOf(item1, item2, item3, item4, item11, item12, item13, item14))

        viewModel = ViewModelProvider(this).get(SectionHeaderExampleViewModel::class.java)
        viewModel.listWithSectionHeaders = list

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