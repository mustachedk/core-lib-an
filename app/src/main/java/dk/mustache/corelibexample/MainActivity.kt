package dk.mustache.corelibexample

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import dk.mustache.corelib.list_header_viewpager.*
import dk.mustache.corelib.utils.*
import dk.mustache.corelibexample.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), LocationUtil.LocationChangedCallback {

    private var locationUtil: LocationUtil? = null
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        binding.del = this

        //region HeaderListViewPager testdata
        val t1 = SpecialData("t1")
        t1.topListItemText = "test1 testtkhjgsklfgh;k "
        val t2 = SpecialData("t2")
        t2.topListItemText = "test2 lkjlkjoitmkgj f"
        val t3 = SpecialData("t3")
        t3.topListItemText = "test3"
        val t4 = SpecialData("1234567890")
        t4.topListItemText = "test4 testtttt"
        val t5 = SpecialData("2345678901")
        t5.topListItemText = "testerkjhksdfgkj figkj"

        val t6 = SpecialData("6825643")
        t6.topListItemText = "983465"
        val t7 = SpecialData("89302")
        t7.topListItemText = "3435iji54793457345958678954"
        val t8 = SpecialData("89892345")
        t8.topListItemText = "13746"

        //SCROLL TYPE
        val fragment = HeaderListViewPagerFragment.newInstance(PageList(
            listOf(t1,t2,t3/*,t4,t5*/)),
            PagerFragment::class.java,
            HeaderListViewPagerSettings(10.toPx(), HeaderListViewPagerTypeEnum.SCROLL, R.layout.top_list_scroll_item)
        )

        //STRETCH type
//        val fragment = HeaderListViewPagerFragment.newInstance(PageList(
//            listOf(t1,t2,t3/*,t4,t5*/)),
//            PagerFragment::class.java,
//            HeaderListViewPagerSettings(10.toPx(), HeaderListViewPagerTypeEnum.SCROLL, R.layout.top_list_scroll_item)
//        )

        setFragment(fragment)
        Handler().postDelayed({
            //test of data update
            val viewModel = ViewModelProvider(this).get(HeaderListViewPagerViewModel::class.java)
            viewModel.pageDataListObservable.set(listOf(t6,t7,t8,t4,t5))
        }, 5000)

        //endRegion

    }


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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            RC_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasPermission(this, ACCESS_FINE_LOCATION)) {
                        /** User granted location permission */
                    } else {
                        /** We don't have the location permission */
                    }
                } else {
                    /** User denied location permission */
                }
            }
        }
    }

    override fun locationChanged(location: Location) {
        currentLocation = location
    }
}
