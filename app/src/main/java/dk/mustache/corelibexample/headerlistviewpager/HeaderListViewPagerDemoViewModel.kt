package dk.mustache.corelibexample.headerlistviewpager

import androidx.lifecycle.ViewModel
import dk.mustache.corelib.list_header_viewpager.HeaderListViewPagerSettings
import dk.mustache.corelib.list_header_viewpager.HeaderListViewPagerTypeEnum
import dk.mustache.corelib.list_header_viewpager.HeaderListViewPagerViewModel
import dk.mustache.corelib.utils.toPx
import dk.mustache.corelibexample.R

class HeaderListViewPagerDemoViewModel: ViewModel() {
    val data = listOf(
        DemoPageData("Page 1 Header", "Page 1 Content", DemoPagerFragment::class.java, "Page 1 Demo"),
        DemoPageData("Page 2 Header", "Page 2 Content", DemoPagerFragment::class.java, "Page 2 Demo"),
        DemoPageData("Some title of some import", "The sweep of history has all been leading up to this my comrades. From the invention of dice in ancient Egypt. The introduction of wargames in the 19th century. The rise of the first proto-roleplaying games in the 1970s.\n" +
                "\n" +
                "It is time to rise up comrades! No longer shall our schedules be bound by the \"work\" hours of our capitalist overlords. Nor by the artificial strictures of \"family time\".\n" +
                "\n" +
                "No! Once the gamer's revolution is complete, we shall be able to set our schedules according to playing the damn game. And everyone shall be able to play their fill.", DemoPagerFragment::class.java, "Longer Page 3 Demo"),
        DemoPageData("The other title that is long", "The sweep of history has all been leading up to this my comrades. From the invention of dice in ancient Egypt. The introduction of wargames in the 19th century. The rise of the first proto-roleplaying games in the 1970s.\n" +
                "\n" +
                "It is time to rise up comrades! No longer shall our schedules be bound by the \"work\" hours of our capitalist overlords. Nor by the artificial strictures of \"family time\".\n" +
                "\n" +
                "No! Once the gamer's revolution is complete, we shall be able to set our schedules according to playing the damn game. And everyone shall be able to play their fill.", DemoPagerFragment::class.java, "Longer Page 4 Demo"),
        DemoPageData("Page 5 Header", "Page 5 Content", DemoPagerFragment::class.java, "Page 5 Demo"),
        DemoPageData("Page 6 Header", "Page 6 Content", DemoPagerFragment::class.java, "Page 6 Demo")
    )

    fun setup(headerListViewModel: HeaderListViewPagerViewModel) {
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
    }

    fun updateData(headerListViewModel: HeaderListViewPagerViewModel) {
        headerListViewModel.updatePageDataList(data)
    }
}