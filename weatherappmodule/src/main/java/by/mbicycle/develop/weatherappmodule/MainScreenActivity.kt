package by.mbicycle.develop.weatherappmodule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import by.mbicycle.develop.weatherappmodule.databinding.ActivityMainScreenBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class MainScreenActivity : AppCompatActivity(), BottomBarVisibilityListener, SwipeRefreshListener {
    private lateinit var binding: ActivityMainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.apply {
            messageNoLocation.root.visibility = View.GONE
            mainScreenViewPager.apply {
                adapter = MainScreenAdapter(this@MainScreenActivity)
                isUserInputEnabled = false
            }

            TabLayoutMediator(tabLayout, mainScreenViewPager) { tab, position ->
                tab.icon = when(position) {
                    0 -> AppCompatResources.getDrawable(this@MainScreenActivity, R.drawable.ic_city_tab)
                    1 -> AppCompatResources.getDrawable(this@MainScreenActivity, R.drawable.ic_daily_tab)
                    2 -> AppCompatResources.getDrawable(this@MainScreenActivity, R.drawable.ic_hourly_tab)
                    else -> throw IllegalStateException()
                }

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
            LocationController(this@MainScreenActivity).requestLocationUpdate { location ->
                saveData(CoordinatesKeys.LONGITUDE, location.longitude.toFloat())
                saveData(CoordinatesKeys.LATITUDE, location.latitude.toFloat())

                updateDataIntoSomeFragment()
            }
        }
    }

    private fun updateDataIntoSomeFragment() {
        binding.run {
            (mainScreenViewPager.adapter as? MainScreenAdapter)?.getRecentFragmentBy(
                mainScreenViewPager.currentItem)?.let { fragment ->

                if(fragment is UpdateDataListener) {
                    fragment.reloadData()
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

    override fun updateData() {
        updateDataIntoSomeFragment()
    }
}