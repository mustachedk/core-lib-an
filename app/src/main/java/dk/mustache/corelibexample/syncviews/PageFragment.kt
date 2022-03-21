package dk.mustache.corelibexample.syncviews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dk.mustache.corelibexample.R

class PageFragment : Fragment() {
    private var pageNum: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pageNum = it.getString("pagenum")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_page, container, false)
    }

    override fun onResume() {
        super.onResume()

        view?.findViewById<TextView>(R.id.txtPageNum)?.text = pageNum
    }

    companion object {
        @JvmStatic
        fun newInstance(pageNum: String) =
            PageFragment().apply {
                arguments = Bundle().apply {
                    putString("pagenum", pageNum)
                }
            }
    }
}