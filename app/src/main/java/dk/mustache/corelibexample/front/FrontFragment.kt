package dk.mustache.corelibexample.front

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import dk.mustache.corelibexample.R
import dk.mustache.corelibexample.headerlistviewpager.HeaderListViewPagerDemoFragment
import dk.mustache.corelibexample.interscroll.InterscrollFragment
import dk.mustache.corelibexample.mdate.MdateFragment
import dk.mustache.corelibexample.sandbox.SandboxFragment
import dk.mustache.corelibexample.selectablelist.SelectableListFragment
import dk.mustache.corelibexample.standard_dialog.StandardDialogDemoFragment
import dk.mustache.corelibexample.validation.ValidationFragment


class FrontFragment : Fragment() {

    val demos = listOf<Pair<String, Fragment>>(
        "HeaderListViewPager Demo" to HeaderListViewPagerDemoFragment(),
        "MDate Demo" to MdateFragment(),
        "Interscroll Demo" to InterscrollFragment(),
        "SelectableList" to SelectableListFragment(),
        "Standard Dialog Demo" to StandardDialogDemoFragment(),
        "Validation" to ValidationFragment(),
        "Sandbox (test your libs here)" to SandboxFragment()
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_front, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonList = findViewById<LinearLayout>(R.id.buttonList)

        demos.forEach { pair ->
            val button = Button(requireContext())
            button.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            button.text = pair.first
            button.setOnClickListener { navToScreen(pair.second) }
            button.id = View.generateViewId()
            buttonList.addView(button)
        }
    }

    private fun navToScreen(fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun <T:View?> findViewById(@IdRes id: Int): T {
        return requireView().findViewById(id)
    }
}