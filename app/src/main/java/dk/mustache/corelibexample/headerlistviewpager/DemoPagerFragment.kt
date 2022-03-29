package dk.mustache.corelibexample.headerlistviewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dk.mustache.corelib.list_header_viewpager.GenericPagerFragment
import dk.mustache.corelibexample.databinding.FragmentDemoPagerBinding

class DemoPagerFragment: GenericPagerFragment() {
    lateinit var binding: FragmentDemoPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDemoPagerBinding.inflate(inflater)
        binding.pageData = pageData as DemoPageData<*>
        return binding.root
    }
}