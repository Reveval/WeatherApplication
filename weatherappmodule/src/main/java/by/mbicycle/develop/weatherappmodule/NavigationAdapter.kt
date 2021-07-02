package by.mbicycle.develop.weatherappmodule

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.mbicycle.develop.weatherappmodule.ui.city.CityFragment
import by.mbicycle.develop.weatherappmodule.ui.daily.DailyFragment
import by.mbicycle.develop.weatherappmodule.ui.hourly.HourlyFragment

class NavigationAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> CityFragment()
            1 -> DailyFragment()
            2 -> HourlyFragment()
            else -> CityFragment()
        }
    }
}