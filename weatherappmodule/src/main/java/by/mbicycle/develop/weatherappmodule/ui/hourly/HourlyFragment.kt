package by.mbicycle.develop.weatherappmodule.ui.hourly

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.mbicycle.develop.weatherappmodule.R
import by.mbicycle.develop.weatherappmodule.UpdateDataListener
import by.mbicycle.develop.weatherappmodule.databinding.FragmentHourlyBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HourlyFragment : Fragment(), UpdateDataListener {
    lateinit var binding: FragmentHourlyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHourlyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.hourlyViewPager.apply {
            adapter = HourlyNavigationAdapter(this@HourlyFragment)
            currentItem = 2
        }

        TabLayoutMediator(binding.hourlyTabLayout, binding.hourlyViewPager) { tab, position ->
            tab.text = when(position) {
                0 -> getString(R.string.yesterday_tab_name)
                1 -> getString(R.string.today_tab_name)
                else -> throw IllegalStateException()
            }
        }.attach()
    }

    override fun onResume() {
        super.onResume()

        setCityNameAndDate(1)

        binding.hourlyTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { setCityNameAndDate(it.position) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setCityNameAndDate(position: Int) {
        Handler(Looper.getMainLooper()).post {
            context?.let {
                val prefs = PreferencesForHourlyTab.instance(it)
                binding.hourlyCityAndDateTextView.text = when(position) {
                    0 -> prefs.loadData(HourlyTabsPrefsKeys.YESTERDAY)
                    1 -> prefs.loadData(HourlyTabsPrefsKeys.TODAY)
                    else -> ""
                }
            }
        }
    }

    override fun reloadData() {
        binding.run {
            (hourlyViewPager.adapter as? HourlyNavigationAdapter)?.getRecentFragmentBy(
                hourlyViewPager.currentItem)?.let { fragment ->

                if (fragment is UpdateDataListener) {
                    fragment.reloadData()
                }
            }
        }
    }
}