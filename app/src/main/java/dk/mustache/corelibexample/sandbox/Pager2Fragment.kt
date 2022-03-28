package dk.mustache.corelibexample.sandbox

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.mustache.corelib.list_header_viewpager.GenericPagerFragment
import dk.mustache.corelibexample.databinding.FragmentListHeaderPagerBinding

open class Pager2Fragment : GenericPagerFragment() {

    lateinit var binding: FragmentListHeaderPagerBinding

    override fun update() {

    }

    override fun scrollToTop() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentListHeaderPagerBinding.inflate(inflater, container, false)
        binding.pageData = pageData as SpecialData

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Handler().postDelayed({
//            binding.animatedProgressbar.setBackgroundAnimationEnabled(false)
        },5000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}