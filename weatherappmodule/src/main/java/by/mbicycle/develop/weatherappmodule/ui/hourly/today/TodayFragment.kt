package by.mbicycle.develop.weatherappmodule.ui.hourly.today

import android.annotation.SuppressLint
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

class TodayFragment private constructor() : Fragment(), UpdateDataListener {
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

        binding.todaySwipeRefresh.apply {
            setOnRefreshListener {
                activity?.let {
                    if (it is SwipeRefreshListener) {
                        it.updateData()
                    }
                }

                if (isRefreshing) isRefreshing = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showHourlyForecast()
    }

    @SuppressLint("MissingPermission")
    private fun showHourlyForecast() {
        val retrofitManager = context?.let { RetrofitManagerForHourlyForecast(it) } ?:
            RetrofitManagerForHourlyForecast(requireContext())
        val listOfHourlyForecastItems = arrayListOf<HourlyForecastItem>()

        context?.let { ctx ->
            changeProgressBarVisibility(View.VISIBLE)

            PreferencesManager.instance(ctx).let { prefs ->
                isNeedToShowMessageNoUpdate(prefs.preferencesIsEmpty())

                val latitude = prefs.loadData(CoordinatesKeys.LATITUDE)
                val longitude = prefs.loadData(CoordinatesKeys.LONGITUDE)

                retrofitManager.getCityName(latitude, longitude) { cityModel ->
                    isNeedToShowMessageNoUpdate(cityModel.cityName.isEmpty())

                    PreferencesForHourlyTab.instance(ctx).apply {
                        saveData(HourlyTabsPrefsKeys.TODAY, cityModel.getCityNameWithFormattedDate())
                    }
                }

                retrofitManager.getHourlyForecast(latitude, longitude) { modelApi ->
                    isNeedToShowMessageNoUpdate(modelApi.listOfHourlyForecast.isEmpty())

                    listOfHourlyForecastItems.clear()

                    modelApi.listOfHourlyForecast.filter { isDateEqualsCurrentDate(it.date) }.forEach { hourly ->
                        listOfHourlyForecastItems.add(hourly.mapToItem())
                    }

                    Handler(Looper.getMainLooper()).post {
                        changeProgressBarVisibility(View.GONE)
                        recyclerAdapter.setData(listOfHourlyForecastItems)
                    }
                }
            }
        }
    }

    private fun isNeedToShowMessageNoUpdate(predicate: Boolean) {
        Handler(Looper.getMainLooper()).post {
            if (predicate) {
                binding.apply {
                    changeProgressBarVisibility(View.GONE)
                    messageCannotUpdateForTodayTab.root.visibility = View.VISIBLE
                }
            } else {
                binding.messageCannotUpdateForTodayTab.root.visibility = View.GONE
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

    override fun reloadData() {
        showHourlyForecast()
    }

    private fun changeProgressBarVisibility(visibility: Int) {
        activity?.let {
            if (it is ProgressBarVisibilityListener) {
                it.setProgressBarVisibility(visibility)
            }
        }
    }
}