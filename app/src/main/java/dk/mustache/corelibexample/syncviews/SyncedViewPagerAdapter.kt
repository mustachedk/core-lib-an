package dk.mustache.corelibexample.syncviews

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SyncedViewPagerAdapter(activity: AppCompatActivity, val itemsCount: Int) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return PageFragment.newInstance((position + 1).toString())
    }
}