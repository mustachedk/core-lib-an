package dk.mustache.corelibexample.interscroll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import dk.mustache.corelib.interscroll.InterscrollHandler
import dk.mustache.corelibexample.R
import dk.mustache.corelibexample.databinding.FragmentInterscrollBinding

class InterscrollFragment : Fragment() {

    private lateinit var binding: FragmentInterscrollBinding
    private lateinit var interscrollHandler: InterscrollHandler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInterscrollBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // The Interscroll handler can handle both asynchronous acquisition of images and
        // display of local images
        val runAsync = true

        val imageView = ImageView(requireContext())
        val item = InterscrollItem()
        val items = listOf(R.color.colorAccent, item, R.color.colorPrimary)
        val adapter = DemoAdapter(items, imageView)

        binding.list.adapter = adapter

        if (runAsync) {
            // Load asynchronous image with Glide
            interscrollHandler = InterscrollHandler(binding.list, imageView) { imageview ->
                Glide
                    .with(requireContext())
                    .load("https://cdn.vox-cdn.com/thumbor/WR9hE8wvdM4hfHysXitls9_bCZI=/0x0:1192x795/1400x1400/filters:focal(596x398:597x399)/cdn.vox-cdn.com/uploads/chorus_asset/file/22312759/rickroll_4k.jpg")
                    .into(imageview)
            }
        } else {
            // Load local drawable
            interscrollHandler = InterscrollHandler(binding.list, imageView, R.drawable.me)
        }
    }

    class InterscrollItem
}