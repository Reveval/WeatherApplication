package by.mbicycle.develop.weatherappmodule.ui.hourly

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.mbicycle.develop.weatherappmodule.ui.hourly.today.TodayFragment
import by.mbicycle.develop.weatherappmodule.ui.hourly.yesterday.YesterdayFragment

class HourlyNavigationAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragments = listOf<Fragment>(YesterdayFragment.instance(), TodayFragment.instance())

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    internal fun getRecentFragmentBy(position: Int) : Fragment? {
        return if (position < itemCount) { fragments[position] } else null
    }
}