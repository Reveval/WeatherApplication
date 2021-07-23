package by.mbicycle.develop.weatherappmodule.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.mbicycle.develop.weatherappmodule.ui.city.CityFragment
import by.mbicycle.develop.weatherappmodule.ui.city.RootCityTabFragment
import by.mbicycle.develop.weatherappmodule.ui.daily.DailyFragment
import by.mbicycle.develop.weatherappmodule.ui.hourly.HourlyFragment

class MainScreenAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val fragments = listOf<Fragment>(RootCityTabFragment(), DailyFragment(), HourlyFragment())

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    internal fun getRecentFragmentBy(position: Int) : Fragment? {
        return if (position < itemCount) { fragments[position] } else null
    }
}