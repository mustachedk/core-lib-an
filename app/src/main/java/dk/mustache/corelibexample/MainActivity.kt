package dk.mustache.corelibexample

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Application
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import dk.mustache.corelib.MustacheCoreLib
import dk.mustache.corelib.beacon.BeaconScanActivity
import dk.mustache.corelib.bottomsheet_picker.BottomSheetDoublePicker
import dk.mustache.corelib.bottomsheet_picker.BottomSheetPicker
import dk.mustache.corelib.fragment_dialog.DialogTypeEnum
import dk.mustache.corelib.fragment_dialog.FragmentDialogSetup
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
import dk.mustache.corelib.swipe_accept_layout.SwipeAcceptLayout
import dk.mustache.corelib.utils.*
import dk.mustache.corelib.views.EmptyStateView
import dk.mustache.corelibexample.bottomsheets.BottomSheetMenuFragment
import dk.mustache.corelibexample.databinding.ActivityMainBinding
import dk.mustache.corelibexample.model.MockResponse
import dk.mustache.corelibexample.standard_dialog_example.StandardDialogExampleFragment
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
    BottomSheetDoublePicker.BottomSheetPickerListener<PickerTypeEnum>{

    private val disposables: CompositeDisposable = CompositeDisposable()
    private var locationUtil: LocationUtil? = null
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MustacheCoreLib.init(applicationContext as Application)

        locationUtil = LocationUtil(this)
        locationUtil?.registerLocationListener(this)
        requestPermissionWithRationale(
            this,
            ACCESS_FINE_LOCATION,
            RC_ACCESS_FINE_LOCATION,
            "Test Title",
            "Test Message",
            "Test Button"
        )

        var binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )

        binding.mainEmptystate.clickListener = this

        //region HeaderListViewPager testdata

        val viewModel = ViewModelProvider(this).get(HeaderListViewPagerViewModel::class.java)

        val t1 = SpecialData("t1", Pager2Fragment::class.java,"test1 testtkhjgsklfgh;k ")
        val t2 = SpecialData("t2", Pager2Fragment::class.java,"test2 lkjlkjoitmkgj fjdgkfhjkdjfgh hkjfhgkjdhfgkjh ")
        val t3 = SpecialData("t3", PagerFragment::class.java, "test3")
        val t4 = SpecialData("1234567890", CoursesFragment::class.java, "test4 testtttt")
        val t5 = SpecialData("2345678901", PagerFragment::class.java, "testerkjhksdfgkj figkj")
        val t6 = SpecialData("6825643", PagerFragment::class.java, "983465")
        val t7 = SpecialData("89302", Pager2Fragment::class.java, "3435iji54793457345958678954")
        val t8 = SpecialData("89892345", Pager2Fragment::class.java, "13746")

        viewModel.updatePageDataList(listOf(t1, t2, t3, t4, t5, t6, t7, t8))
//        viewModel.updatePageDataList(listOf(t6, t4, t5))

        viewModel.selectedIndexObservable.set(3)
        //SCROLL TYPE
        viewModel.settings.set(
            HeaderListViewPagerSettings(
                paddingBetween = 10.toPx(),
                type = HeaderListViewPagerTypeEnum.SCROLL,
                filterLayoutId = R.layout.top_list_scroll_item,
                lastItemPaddingEnd = 10.toPx(),
                snapCenter = true,
                swipeSensitivity = 1
            )
        )

        //STRETCH type
//        viewModel.settings.set(HeaderListViewPagerSettings(
//            10.toPx(),
//            HeaderListViewPagerTypeEnum.STRETCH,
//            R.layout.top_list_scroll_item
//        ))
        val fragment = HeaderListViewPagerFragment.newInstance()

        //Uncomment to start HeaderListViewPagerFragment
        setFragment(fragment)

        //TEST of update of HeaderListViewPagerFragment
//        Handler(Looper.getMainLooper()).postDelayed({
//            //test of data update
//            val viewModel = ViewModelProvider(this).get(HeaderListViewPagerViewModel::class.java)
//            viewModel.updatePageDataList(listOf(t6, t7, t8, t4, t5))
//        }, 5000)

        //TEST of swipe accept layout
        binding.swipeAcceptLayout.swipeListener = object: SwipeAcceptLayout.SwipeListener {
            override fun onSwipeAccept() {
                Toast.makeText(MustacheCoreLib.getContextCheckInit(), "Swipe Accepted!", Toast.LENGTH_LONG).show()
            }

            override fun onSwipeStarted() {
                Toast.makeText(MustacheCoreLib.getContextCheckInit(), "Swipe initiated!", Toast.LENGTH_SHORT).show()
            }

        }

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

        testRetroFit()
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

    private fun setFragment(fragment: Fragment) {
        val t: FragmentTransaction =
            supportFragmentManager.beginTransaction()
        t.replace(R.id.test_fragment_container, fragment)
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
