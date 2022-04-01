package dk.mustache.corelibexample.headerlistviewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dk.mustache.corelib.list_header_viewpager.HeaderListViewPagerSettings
import dk.mustache.corelib.list_header_viewpager.HeaderListViewPagerTypeEnum
import dk.mustache.corelib.list_header_viewpager.HeaderListViewPagerViewModel
import dk.mustache.corelib.utils.toPx
import dk.mustache.corelibexample.R
import dk.mustache.corelibexample.databinding.FragmentHeaderListViewpagerDemoBinding

class HeaderListViewPagerDemoFragment : Fragment() {

    lateinit var viewModel: HeaderListViewPagerDemoViewModel
    lateinit var headerListViewModel: HeaderListViewPagerViewModel

    lateinit var binding: FragmentHeaderListViewpagerDemoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeaderListViewpagerDemoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =
            ViewModelProvider(this).get(HeaderListViewPagerDemoViewModel::class.java)
        headerListViewModel = HeaderListViewPagerViewModel.getInstance(requireActivity())

        headerListViewModel.settings.set(
            HeaderListViewPagerSettings(
                type = HeaderListViewPagerTypeEnum.SCROLL,
                filterBackgroundColor = R.color.colorPrimaryDark,
                paddingBetweenItems = 8.toPx(),
                firstItemPaddingStart = 8.toPx(),
                lastItemPaddingEnd = 8.toPx(),
                snapCenter = true,
                swipeSensitivity = 4
            )
        )
        headerListViewModel.currentShownPage = 0

        viewModel.acquireData { data ->
            headerListViewModel.updatePageDataList(data)
        }


    }
}