package by.mbicycle.develop.weatherappmodule.ui.hourly.yesterday

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.mbicycle.develop.weatherappmodule.*
import by.mbicycle.develop.weatherappmodule.databinding.FragmentYesterdayBinding
import by.mbicycle.develop.weatherappmodule.ui.hourly.*
import java.io.File
import java.util.*

class YesterdayFragment private constructor() : Fragment(), UpdateDataListener {
    private lateinit var binding: FragmentYesterdayBinding
    private val recyclerAdapter = RecyclerAdapterForHourlyTab()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentYesterdayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerForYesterdayTab.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = recyclerAdapter
            overScrollMode = View.OVER_SCROLL_NEVER
        }

        binding.yesterdaySwipeRefresh.apply {
            setOnRefreshListener {
                activity?.let {
                    if(it is SwipeRefreshListener) {
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

    private fun showHourlyForecast() {
        val currentDateWithoutTimeInMillis = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val yesterdayDateInMillis = currentDateWithoutTimeInMillis - 86400000L
        val currentDateWithPostfix = "$currentDateWithoutTimeInMillis.json"
        val yesterdayDateWithPostfix = "$yesterdayDateInMillis.json"

        context?.let { context ->
            File(context.filesDir, "/").listFiles()?.let { listOfFiles ->
                if (listOfFiles.isEmpty()) {
                    readDataFromJson(context, currentDateWithPostfix)
                } else if (listOfFiles[0].name.equals(currentDateWithPostfix) || listOfFiles[0].name.equals(yesterdayDateWithPostfix)) {
                    File(context.filesDir, "/").listFiles()?.let {
                        changeProgressBarVisibility(View.VISIBLE)
                        JsonHelper.readDataFromJson(context, it[0].name) { items ->
                            Handler(Looper.getMainLooper()).post {
                                changeProgressBarVisibility(View.GONE)
                                recyclerAdapter.setData(items)
                            }
                        }
                    }
                } else {
                    listOfFiles[0].delete()
                    readDataFromJson(context, currentDateWithPostfix)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun writeHourlyForecastDataIntoJson(
        context: Context,
        currentDate: String,
        done: (Boolean) -> Unit) {

        val retrofitManager = RetrofitManagerForHourlyForecast(context)
        val listOfHourlyForecastItems = arrayListOf<HourlyForecastItem>()
        val currentDateInMillis = currentDate.replace(".json", "").toLong()

        PreferencesManager.instance(context).let { prefs ->
            isNeedToShowMessageNoUpdate(prefs.preferencesIsEmpty())

            val latitude = prefs.loadData(CoordinatesKeys.LATITUDE)
            val longitude = prefs.loadData(CoordinatesKeys.LONGITUDE)

            retrofitManager.getCityName(latitude, longitude) { cityModel ->
                isNeedToShowMessageNoUpdate(cityModel.cityName.isEmpty())

                PreferencesForHourlyTab.instance(context).apply{
                    saveData(HourlyTabsPrefsKeys.YESTERDAY, cityModel.getCityNameWithFormattedDate())
                }
            }

            retrofitManager.getHourlyForecast(latitude, longitude) { modelApi ->
                isNeedToShowMessageNoUpdate(modelApi.listOfHourlyForecast.isEmpty())

                listOfHourlyForecastItems.clear()
                modelApi.listOfHourlyForecast.filter {
                    isDateEqualsCurrentDate(currentDateInMillis, it.date) }.forEach {
                        hourly -> listOfHourlyForecastItems.add(hourly.mapToItem())
                }

                Handler(Looper.getMainLooper()).post {
                    JsonHelper.exportToJson(context, currentDate, listOfHourlyForecastItems, done)
                }
            }
        }
    }

    private fun isNeedToShowMessageNoUpdate(predicate: Boolean) {
        Handler(Looper.getMainLooper()).post {
            if (predicate) {
                binding.apply {
                    changeProgressBarVisibility(View.GONE)
                    messageCannotGetUpdateForYesterdayTab.root.visibility = View.VISIBLE
                }
            } else {
                binding.messageCannotGetUpdateForYesterdayTab.root.visibility = View.GONE
            }
        }
    }

    private fun readDataFromJson(context: Context, currentDate: String) {
        changeProgressBarVisibility(View.VISIBLE)
        writeHourlyForecastDataIntoJson(context, currentDate) { result ->
            if (result) {
                File(context.filesDir, "/").listFiles()?.let { files ->
                    JsonHelper.readDataFromJson(context, files[0].name) { items ->
                        Handler(Looper.getMainLooper()).post {
                            changeProgressBarVisibility(View.GONE)
                            recyclerAdapter.setData(items)
                        }
                    }
                }
            }
        }
    }

    private fun isDateEqualsCurrentDate(currentDate: Long, comparedDate: Long) : Boolean {
        val calcDate = Calendar.getInstance().run {
            time = Date(comparedDate * 1000L)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            timeInMillis
        }
        return currentDate == calcDate
    }

    companion object {
        fun instance() : YesterdayFragment {
            return YesterdayFragment()
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