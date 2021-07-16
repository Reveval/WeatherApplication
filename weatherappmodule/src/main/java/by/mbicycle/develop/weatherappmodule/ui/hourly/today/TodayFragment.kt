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
import by.mbicycle.develop.weatherappmodule.*
import by.mbicycle.develop.weatherappmodule.databinding.FragmentTodayBinding
import by.mbicycle.develop.weatherappmodule.ui.hourly.*
import java.util.*

class TodayFragment private constructor() : Fragment(), UpdateLocationListener {
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

        context?.let { ctx ->
            PreferencesManager.instance(ctx).let { prefs ->
                if (prefs.preferencesIsEmpty()) return

                val latitude = prefs.loadData(CoordinatesKeys.LATITUDE)
                val longitude = prefs.loadData(CoordinatesKeys.LONGITUDE)

                retrofitManager.getCityName(latitude, longitude) { cityModel ->
                    PreferencesForHourlyTab.instance(ctx).apply {
                        saveData(HourlyTabsPrefsKeys.TODAY, cityModel.getCityNameWithFormattedDate())
                    }
                }

                retrofitManager.getHourlyForecast(latitude, longitude) { modelApi ->
                    listOfHourlyForecastItems.clear()

                    modelApi.listOfHourlyForecast.filter { isDateEqualsCurrentDate(it.date) }.forEach { hourly ->
                        listOfHourlyForecastItems.add(hourly.mapToItem())
                    }

                    Handler(Looper.getMainLooper()).post {
                        recyclerAdapter.setData(listOfHourlyForecastItems)
                    }
                }
            }
        }
    }

    private fun isDateEqualsCurrentDate(date: Long) : Boolean {
        val currentDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val calcDate = Calendar.getInstance().run {
            time = Date(date * 1000L)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            timeInMillis
        }

        return currentDate == calcDate
    }

    companion object {
        fun instance() : TodayFragment {
            return TodayFragment()
        }
    }

    override fun loadLocationData() {
        showHourlyForecast()
    }
}