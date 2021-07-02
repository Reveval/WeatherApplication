package by.mbicycle.develop.weatherappmodule.ui.hourly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.mbicycle.develop.weatherappmodule.R

class HourlyFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hourly, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        fun createHourlyFragmentInstance() : HourlyFragment {
            val hourlyFragment = HourlyFragment()
            return hourlyFragment
        }
    }
}