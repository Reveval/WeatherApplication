package by.mbicycle.develop.weatherappmodule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.viewpager2.widget.ViewPager2
import by.mbicycle.develop.weatherappmodule.ui.city.CityFragment
import by.mbicycle.develop.weatherappmodule.ui.city.InformationFragment
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), BottomBarVisibilityListener {
    lateinit var cityViewPager: ViewPager2
    lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cityViewPager = findViewById(R.id.view_pager)
        cityViewPager.isUserInputEnabled = false
        tabLayout = findViewById(R.id.tab_layout)

        val navigationAdapter = NavigationAdapter(this)
        cityViewPager.adapter = navigationAdapter

        TabLayoutMediator(tabLayout, cityViewPager) {
            tab, position -> tab.text = when(position) {
                0 -> getString(R.string.city_tab_name)
                1 -> getString(R.string.daily_tab_name)
                2 -> getString(R.string.hourly_tab_name)
                else -> throw IllegalStateException()
            }
        }.attach()
    }

    override fun setBottomBarVisibility(visibility: Int) {
        tabLayout.visibility = visibility
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager.fragments.first().childFragmentManager
        if (fragmentManager.fragments.size == 1) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}