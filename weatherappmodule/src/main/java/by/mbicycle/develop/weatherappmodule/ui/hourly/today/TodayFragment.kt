package by.mbicycle.develop.weatherappmodule.ui.hourly.today

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.mbicycle.develop.weatherappmodule.databinding.FragmentTodayBinding
import by.mbicycle.develop.weatherappmodule.mapToItem
import by.mbicycle.develop.weatherappmodule.ui.hourly.HourlyForecastItem
import by.mbicycle.develop.weatherappmodule.ui.hourly.RecyclerAdapterForHourlyTab
import by.mbicycle.develop.weatherappmodule.ui.hourly.RetrofitManagerForHourlyForecast

class TodayFragment private constructor() : Fragment() {
    private lateinit var binding: FragmentTodayBinding
    private val recyclerAdapter = RecyclerAdapterForHourlyTab()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerForTodayTab.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = recyclerAdapter
            overScrollMode = View.OVER_SCROLL_NEVER
        }

        showHourlyForecast()
    }

    @SuppressLint("MissingPermission")
    private fun showHourlyForecast() {
        val retrofitManager = RetrofitManagerForHourlyForecast()
        val listOfHourlyForecastItems = arrayListOf<HourlyForecastItem>()

        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
            retrofitManager.getHourlyForecast(it.latitude, it.longitude) { modelApi ->
                listOfHourlyForecastItems.clear()
                modelApi.listOfHourlyForecast.forEach { hourly ->
                    listOfHourlyForecastItems.add(hourly.mapToItem())
                }

                Handler(Looper.getMainLooper()).post {
                    recyclerAdapter.setData(listOfHourlyForecastItems)
                }
            }
        }
    }

    companion object {
        fun instance() : TodayFragment {
            return TodayFragment()
        }
    }
}