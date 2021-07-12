package by.mbicycle.develop.weatherappmodule.ui.hourly

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.mbicycle.develop.weatherappmodule.ui.hourly.today.TodayFragment
import by.mbicycle.develop.weatherappmodule.ui.hourly.yesterday.YesterdayFragment

class HourlyNavigationAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> YesterdayFragment.instance()
            1 -> TodayFragment.instance()
            else -> throw IllegalStateException()
        }
    }

}