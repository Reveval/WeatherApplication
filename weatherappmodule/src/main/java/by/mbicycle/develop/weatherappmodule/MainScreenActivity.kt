package by.mbicycle.develop.weatherappmodule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import by.mbicycle.develop.weatherappmodule.databinding.ActivityMainScreenBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class MainScreenActivity : AppCompatActivity(), BottomBarVisibilityListener {
    private lateinit var binding: ActivityMainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            messageNoData.root.visibility = View.GONE
            viewPager.apply {
                adapter = MainScreenAdapter(this@MainScreenActivity)
                isUserInputEnabled = false
            }

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when(position) {
                    0 -> getString(R.string.city_tab_name)
                    1 -> getString(R.string.daily_tab_name)
                    2 -> getString(R.string.hourly_tab_name)
                    else -> throw IllegalStateException()
                }
            }.attach()
        }

        if (PreferencesManager.instance(this).preferencesIsEmpty()) {
            getRecentLocation()
        }
    }

    private fun getRecentLocation() {
        PreferencesManager.instance(this).apply {
            LocationController(this@MainScreenActivity) { location ->
                saveData(CoordinatesKeys.LONGITUDE, location.longitude.toFloat())
                saveData(CoordinatesKeys.LATITUDE, location.latitude.toFloat())
            }
        }

        binding.run {
            (viewPager.adapter as? MainScreenAdapter)?.getRecentFragmentBy(
                viewPager.currentItem)?.let { fragment ->

                if(fragment is UpdateLocationListener) {
                    fragment.loadLocationData()
                }
            }
        }
    }

    override fun setBottomBarVisibility(visibility: Int) {
        binding.tabLayout.visibility = visibility
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager.fragments.first().childFragmentManager
        if (fragmentManager.backStackEntryCount == 2) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}