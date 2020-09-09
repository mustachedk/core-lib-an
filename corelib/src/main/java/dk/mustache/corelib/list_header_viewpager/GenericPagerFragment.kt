package dk.mustache.corelib.list_header_viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment

class GenericPagerFragment : Fragment() {


    companion object {
        const val ARG_POG_ENUM = "arg_pog_index"

        @JvmStatic
        fun newInstance(pageData: PageData) =
            GenericPagerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_POG_ENUM, pageData)
                }
            }
    }
}