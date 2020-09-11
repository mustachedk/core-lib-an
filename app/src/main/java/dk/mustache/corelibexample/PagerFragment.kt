package dk.mustache.corelibexample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.mustache.corelib.list_header_viewpager.GenericPagerFragment
import dk.mustache.corelibexample.databinding.FragmentListHeaderPagerBinding

open class PagerFragment : GenericPagerFragment<SpecialData>() {

    lateinit var binding: FragmentListHeaderPagerBinding

    override fun update() {

    }

    override fun scrollToTop() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentListHeaderPagerBinding.inflate(inflater, container, false)
        binding.pageData = pageData

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}