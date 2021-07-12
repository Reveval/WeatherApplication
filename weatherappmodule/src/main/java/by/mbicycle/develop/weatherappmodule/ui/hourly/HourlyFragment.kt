package by.mbicycle.develop.weatherappmodule.ui.hourly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.mbicycle.develop.weatherappmodule.R
import by.mbicycle.develop.weatherappmodule.databinding.FragmentHourlyBinding
import com.google.android.material.tabs.TabLayoutMediator

class HourlyFragment : Fragment() {
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
            isUserInputEnabled = false
            adapter = HourlyNavigationAdapter(this@HourlyFragment)
            currentItem = 1
        }

        TabLayoutMediator(binding.hourlyTabLayout, binding.hourlyViewPager) { tab, position ->
            tab.text = when(position) {
                0 -> getString(R.string.yesterday_tab_name)
                1 -> getString(R.string.today_tab_name)
                else -> throw IllegalStateException()
            }
        }.attach()
    }
}