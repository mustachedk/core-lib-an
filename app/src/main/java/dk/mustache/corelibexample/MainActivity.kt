package dk.mustache.corelibexample

import android.app.Application
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import dk.mustache.corelib.MustacheCoreLib
import dk.mustache.corelib.beacon.BeaconScanActivity
import dk.mustache.corelib.bottomsheet_picker.BottomSheetDoublePicker
import dk.mustache.corelib.bottomsheet_picker.BottomSheetPicker
import dk.mustache.corelib.fragment_dialog.DialogTypeEnum
import dk.mustache.corelib.fragment_dialog.StandardDialogFragment
import dk.mustache.corelib.fragment_dialog.StandardDialogFragment.Companion.BUTTON_CANCEL
import dk.mustache.corelib.fragment_dialog.StandardDialogFragment.Companion.BUTTON_OK
import dk.mustache.corelib.fragment_dialog.StandardDialogFragment.Companion.TEXT_CLICKED
import dk.mustache.corelib.list_header_viewpager.HeaderListViewPagerFragment
import dk.mustache.corelib.list_header_viewpager.HeaderListViewPagerSettings
import dk.mustache.corelib.list_header_viewpager.HeaderListViewPagerTypeEnum
import dk.mustache.corelib.list_header_viewpager.HeaderListViewPagerViewModel
import dk.mustache.corelib.menu_bottom_sheet.MenuDialogType
import dk.mustache.corelib.network.AccessToken
import dk.mustache.corelib.network.AuthorizationRepository
import dk.mustache.corelib.network.AuthorizationType
import dk.mustache.corelib.network.RetroClient
import dk.mustache.corelib.swipe_recyclerview_item.SwipeSettingsEnum
import dk.mustache.corelib.utils.*
import dk.mustache.corelib.views.EmptyStateView
import dk.mustache.corelibexample.bottomsheets.BottomSheetMenuFragment
import dk.mustache.corelibexample.databinding.ActivityMainBinding
import dk.mustache.corelibexample.model.MockResponse
import dk.mustache.corelibexample.section_header_example.SectionHeaderExampleViewModel
import dk.mustache.corelibexample.swipe_recyclerview_item_example.SwipeListAdapter
import dk.mustache.corelibexample.swipe_recyclerview_item_example.SwipeSectionExampleItem
import dk.mustache.corelibexample.toolbar_expandable_test.CoursesFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : BeaconScanActivity(),
    BottomSheetMenuFragment.BottomSheetMenuListener,
    StandardDialogFragment.BaseDialogFragmentListener<DialogTypeEnum>,
    LocationUtil.LocationChangedCallback,
    EmptyStateView.OnEmptystateActionListener,
    BottomSheetPicker.BottomSheetPickerListener<PickerTypeEnum>,
    BottomSheetDoublePicker.BottomSheetPickerListener<PickerTypeEnum> {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private var locationUtil: LocationUtil? = null
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MustacheCoreLib.init(applicationContext as Application)

        locationUtil = LocationUtil(this)
        locationUtil?.registerLocationListener(this)
//        requestPermissionWithRationale(
//            this,
//            ACCESS_FINE_LOCATION,
//            RC_ACCESS_FINE_LOCATION,
//            "Test Title",
//            "Test Message",
//            "Test Button"
//        )

        var binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )

        //TODO uncomment to test headerlistviewpager
        val item1 = SwipeSectionExampleItem("test7", 0, false, 1)
        val item2 = SwipeSectionExampleItem("test1", 1, false, 2)
        val item3 = SwipeSectionExampleItem("test1",1, false, 3)
        val item4 = SwipeSectionExampleItem("test2", 2, false, 4)
        val item11 = SwipeSectionExampleItem( "test4",3, false, 5)
        val item12 = SwipeSectionExampleItem("test4",3, false, 6)
        val item13 = SwipeSectionExampleItem("test4", 3, false, 7)
        val item14 = SwipeSectionExampleItem("test5", 4, false, 8)
        val item15 = SwipeSectionExampleItem("test7", 0, false, 9)
        val item16 = SwipeSectionExampleItem("test1", 1, false, 10)
        val item17 = SwipeSectionExampleItem("test1",1, false, 11)
        val item18 = SwipeSectionExampleItem("test2", 2, false, 12)
        val item19 = SwipeSectionExampleItem( "test4",3, false, 13)
        val item20 = SwipeSectionExampleItem("test4",3, false, 14)
        val item21 = SwipeSectionExampleItem("test4", 3, false, 15)
        val item22 = SwipeSectionExampleItem("test5", 4, false, 16)
        val item23 = SwipeSectionExampleItem("test1", 1, false, 17)
        val item24 = SwipeSectionExampleItem("test1",1, false, 18)
        val item25 = SwipeSectionExampleItem("test2", 2, false, 19)
        val item26 = SwipeSectionExampleItem( "test4",3, false, 20)
        val item27 = SwipeSectionExampleItem("test4",3, false, 21)
        val item28 = SwipeSectionExampleItem("test4", 3, false, 22)
        val item29 = SwipeSectionExampleItem("test5", 4, false, 23)
        val item30 = SwipeSectionExampleItem("test7", 0, false, 24)
        val item31 = SwipeSectionExampleItem("test1", 1, false, 25)
        val item32 = SwipeSectionExampleItem("test1",1, false, 26)
        val item33 = SwipeSectionExampleItem("test2", 2, false, 27)
        val item34 = SwipeSectionExampleItem( "test4",3, false, 28)
        val item35 = SwipeSectionExampleItem("test4",3, false, 29)
        val item36 = SwipeSectionExampleItem("test4", 3, false, 30)
        val item37 = SwipeSectionExampleItem("test5", 4, false, 31)
        val list = ArrayList(listOf(item1, item2, item3, item4, item11, item12, item13, item14, item15, item16, item17, item18, item19, item20, item21, item22, item23, item24, item25, item26, item27, item28, item29, item30, item31, item32, item33, item34, item35, item36, item37))

        val viewModel2 = ViewModelProvider(this).get(SectionHeaderExampleViewModel::class.java)
//        viewModel2.listWithSectionHeaders = list
//
//        headerListViewPagerViewModel = ViewModelProvider(requireActivity()).get(
//            HeaderListViewPagerViewModel::class.java)
//        binding.offerTypeList.addOnScrollListener(object : RecyclerView.OnScrollListener(){
//
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                totalScroll += dy
//                headerListViewPagerViewModel.settings.get()?.setTopListTranslationY(totalScroll)
//            }
//        })
//
//
        val adapter = SwipeListAdapter(list, viewModel2, SwipeSettingsEnum.BOTH, R.layout.swipe_section_row_item, R.layout.swipe_section_row_item, R.layout.section_header_item, {
            //onClick
        }, { swipeLayout, swipeDirection, swipeItem ->
            //swiped
        }, { swipeLayout, swipeDirection, swipeItem ->
            //is swiping
        }) { doLockScroll ->

        }

//        val stickyDecoration = StickyHeaderItemDecoration(binding.offerTypeList, R.layout.section_header_item, adapter)
//        binding.offerTypeList.addItemDecoration(stickyDecoration)
//
//        (binding.offerTypeList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
//
//        binding.offerTypeList.adapter = adapter
//        binding.offerTypeList.layoutManager = LinearLayoutManager(MustacheCoreLib.getContextCheckInit())

//        BarcodeBitmapCreator.createBarcodeBitmapFromString("9781782808084", BarcodeFormat.EAN_13, 300.toPx(), 160.toPx()) {
//              binding.barcodeLayout.setImageBitmap(it)
//        }

        adapter.updateDataAndAddHeaders(list)

//        binding.mainEmptystate.clickListener = this

//        Handler(Looper.getMainLooper()).postDelayed({
//            binding.buttonWithLoading.showLoadingIndicator(true)
//        }, 3000)

        //region HeaderListViewPager testdata

        val viewModel = ViewModelProvider(this).get(HeaderListViewPagerViewModel::class.java)

        val t1 = SpecialData("t1", Pager2Fragment::class.java,"test1 testtkhjgsklfgh;k ")
        val t2 = SpecialData("t2", Pager2Fragment::class.java,"test2 lkjlkjoitmkgj fjdgkfhjkdjfgh jkhhkjhjk hkjfhgkjdhfgkjh ")
        val t3 = SpecialData("t3", PagerFragment::class.java, "test3")
        val t4 = SpecialData("1234567890", CoursesFragment::class.java, "test678")
        val t5 = SpecialData("2345678901", PagerFragment::class.java, "testerkjhksdfgkj figkj")
        val t6 = SpecialData("6825643", PagerFragment::class.java, "983465")
        val t7 = SpecialData("89302", Pager2Fragment::class.java, "3435iji54793457345958678954")
        val t8 = SpecialData("89892345", Pager2Fragment::class.java, "13746")

        viewModel.updatePageDataList(listOf(/*t1, t2, t3, t4, t5,*/ t6, t7, t8))
//        viewModel.updatePageDataList(listOf(t6, t4, t5))

        viewModel.selectedIndexObservable.set(0)


        viewModel.selectedIndexObservable.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val test = viewModel.selectedIndexObservable.get()
                test.toString()
            }

        })
        //SCROLL TYPE
        viewModel.settings.set(
            HeaderListViewPagerSettings(
                type = HeaderListViewPagerTypeEnum.SCROLL,
                filterLayoutId = R.layout.top_list_scroll_item,
                paddingBetweenItems = 10.toPx(),
                firstItemPaddingStart = 10.toPx(),
                lastItemPaddingEnd = 15.toPx(),
                snapCenter = true,
                swipeSensitivity = 1,
                compatibilityModePreVersion123 = false
            )
        )

        //STRETCH type
//        viewModel.settings.set(
//            HeaderListViewPagerSettings(
//                type = HeaderListViewPagerTypeEnum.STRETCH,
//                filterLayoutId = R.layout.top_list_stretch_item,
//                paddingBetweenItems = 30.toPx(),
//                lastItemPaddingEnd = 10.toPx(),
//                firstItemPaddingStart = 10.toPx(),
//                horizontalListHeight = 0.toPx(),
//                snapCenter = true,
//                swipeSensitivity = 1,
//                compatibilityModePreVersion123 = false
//            )
//        )
        val fragment = HeaderListViewPagerFragment.newInstance()

        //Uncomment to start HeaderListViewPagerFragment
        setHeaderListViewPagerFragment(fragment)

        //TEST of update of HeaderListViewPagerFragment
        Handler(Looper.getMainLooper()).postDelayed({

            //test of data update
            val viewModel = ViewModelProvider(this).get(HeaderListViewPagerViewModel::class.java)
            viewModel.updatePageDataList(listOf(t4, t7, t6, t1, t2, t3))
//            viewModel.updatePageDataList(listOf(t4, t7, t6, t1))
            viewModel.selectedIndexObservable.set(4)
        }, 5000)

        binding.buttonWithLoading.setOnClickListener {
            Toast.makeText(this, "testsetstetsgdkfjhk", Toast.LENGTH_LONG).show()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            binding.buttonWithLoading.showLoadingIndicator(true)
        },1000)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.buttonWithLoading.showLoadingIndicator(false)
        },10000)

//
//        Handler(Looper.getMainLooper()).postDelayed({
//            //test of data update
//            val viewModel = ViewModelProvider(this).get(HeaderListViewPagerViewModel::class.java)
//            viewModel.updatePageDataList(listOf(t1, t2, t3, t4))
//            viewModel.selectedIndexObservable.set(3)
//        }, 10000)
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            //test of data update
//            val viewModel = ViewModelProvider(this).get(HeaderListViewPagerViewModel::class.java)
//            viewModel.updatePageDataList(listOf(t8, t7))
//            viewModel.selectedIndexObservable.set(1)
//        }, 15000)

        //TEST of swipe accept layout
//        binding.swipeAcceptLayout.swipeListener = object: SwipeAcceptLayout.SwipeListener {
//            override fun onSwipeAccept() {
//                Toast.makeText(MustacheCoreLib.getContextCheckInit(), "Swipe Accepted!", Toast.LENGTH_LONG).show()
//            }
//
//            override fun onSwipeStarted() {
//                Toast.makeText(MustacheCoreLib.getContextCheckInit(), "Swipe initiated!", Toast.LENGTH_SHORT).show()
//            }
//
//        }

        //BottomSheetMenuFragment uncomment to see usage
//        val menu = BottomSheetMenuFragment.newInstance(BottomSheetDialogSettings("Menu Header", listOf("Menu Option 1", "Menu Option 2", "Menu Option 3", "Menu Option 4"), MenuDialogType.CUSTOM))
//        menu.show(supportFragmentManager, "MenuDialog")


        //BottomSheetPicker test - usage remember to implement BottomSheetPicker.BottomSheetPickerListener in Activity or parentFragement
//        val picker = BottomSheetPicker.newInstance(PickerTypeEnum.TEXT_PICKER,
//                                                   listOf("test1","test2","test3"),
//                                    1,
//                                         "CUSTOM OK",
//                                         "HEADER TEST",
//                                                   R.color.light_gray_background)
//        picker.show(supportFragmentManager, picker.tag)

//        BottomSheetDoublePicker test - usage remember to implement BottomSheetDoublePicker.BottomSheetPickerListener in Activity or parentFragement
//        val picker = BottomSheetDoublePicker.newInstance(PickerTypeEnum.TEXT_PICKER,
//            listOf("d1test1","d1test2","d1test3"),
//            listOf("d2test1","d2test2","d2test3"),
//            0,
//            2,
//            "CUSTOM OK",
//            "HEADER TEST",
//            R.color.light_gray_background)
//        picker.show(supportFragmentManager, picker.tag)

        //BaseDialogFragment
//        val dialog = StandardDialogExampleFragment.newInstance(FragmentDialogSetup(
//            header = "Header",
//            text = "Text",
//            positiveButtonText = "Custom text",
//            negativeButtonText = "test",
//            dialogType = DialogTypeEnum.CUSTOM,
//            alternativeLayout = R.layout.fragment_dialog_custom,
//            alternativeStyle = R.style.FragmentDialogStyle,
//            showNegativeButton = true,
//            setAlternativeStyleIfProvided = true
//        ))
//        dialog.show(supportFragmentManager, "FragmentDialog")

//        testRetroFit()
        //endRegion
    }

    private fun testRetroFit() {
        //configure Retrofit client once on app startup
        val api = RetroClient.buildWebApi(
            "https://jsonplaceholder.typicode.com/",
            WebAPI.MockService::class.java,
            object :
                AuthorizationRepository {
                override fun fetchFreshAccessToken(): AccessToken {
                    return AccessToken("")
                }
            })

        //retrofitInstance can now be used to create web api, call a service within and return an Observable result
        val observable = api.getMockDB(AuthorizationType.ACCESS_TOKEN)

        //subscribe to observable and await results
        observable?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : Observer<MockResponse> {
                override fun onSubscribe(d: Disposable) {
                    Log.d("RXJava", "onSubscribe: ${Thread.currentThread().name}")
                    disposables.add(d)
                }

                override fun onNext(t: MockResponse) {
                    Log.d("RXJava", "onNext: ${Thread.currentThread().name}")
                }

                override fun onError(e: Throwable) {
                    Log.d("RXJava", "onError: ${e}")
                }

                override fun onComplete() {
                    Log.d("RXJava", "onComplete: ${Thread.currentThread().name}")
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    /*enqueue(
    object :
        RetroCallback<MockResponse>() {
        override fun onSuccess(response: MockResponse?, code: Int) {
            Log.d("RetroFit", "onSuccess: $response")
        }

        override fun onError(body: ResponseBody?, code: Int) {
            Log.d("RetroFit", "onFailed: $body")
        }
    })*/

    override fun onResume() {
        super.onResume()
        locationUtil?.startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        locationUtil?.stopLocationUpdates()
    }

    private fun setHeaderListViewPagerFragment(fragment: Fragment) {
            val t: FragmentTransaction =
                supportFragmentManager.beginTransaction()
            t.replace(R.id.test_headerlist_fragment_container, fragment)
            t.commit()
    }

//    @SuppressLint("MissingSuperCall")
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        when (requestCode) {
//            RC_ACCESS_FINE_LOCATION -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (hasPermission(this, ACCESS_FINE_LOCATION)) {
//                        /** User granted location permission */
//                    } else {
//                        /** We don't have the location permission */
//                    }
//                } else {
//                    /** User denied location permission */
//                }
//            }
//        }
//    }

    override fun locationChanged(location: Location) {
        currentLocation = location
    }

    override fun itemSelected(text: String, index: Int, menuType: MenuDialogType) {

    }

    override fun dialogButtonClicked(index: Int, dialogType: DialogTypeEnum) {
        when(index) {
            BUTTON_OK -> {
                Toast.makeText(this, "BUTTON_OK", Toast.LENGTH_LONG).show()
            }
            BUTTON_CANCEL -> {
                Toast.makeText(this, "BUTTON_CANCEL", Toast.LENGTH_LONG).show()
            }
            TEXT_CLICKED -> {
                Toast.makeText(this, "TEXT_CLICKED", Toast.LENGTH_LONG).show()
            }
            else -> {

            }
        }
    }

//    override fun pickerItemSelected(paramType: PickerTypeEnum?, value: String, selectedIndex: Int) {
//        when(paramType) {
//            PickerTypeEnum.TEXT_PICKER -> {
//                //Do something
//                Toast.makeText(this, paramType.toString() + " " + value + " " + selectedIndex, Toast.LENGTH_LONG).show()
//            }
//            PickerTypeEnum.NUMBER_PICKER -> {
//                //Do something else
//                Toast.makeText(this, paramType.toString(), Toast.LENGTH_LONG).show()
//            }
//            else -> {
//
//            }
//        }
//    }

    override fun nothingSelected() {

    }

    override fun onEmptystateClicked() {
        Toast.makeText(this, "Emptystate clicked", Toast.LENGTH_LONG).show()
    }

    override fun pickerItemSelected(paramType: PickerTypeEnum?, value: String, selectedIndex: Int) {

        Toast.makeText(this, value, Toast.LENGTH_LONG).show()
    }

    override fun pickerItemSelected(paramType: PickerTypeEnum?, values: List<String>) {
        val str = values.get(0) + " " + values.get(1)
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
    }
}
