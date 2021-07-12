package by.mbicycle.develop.weatherappmodule.ui.hourly.yesterday

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
import by.mbicycle.develop.weatherappmodule.databinding.FragmentYesterdayBinding
import by.mbicycle.develop.weatherappmodule.mapToItem
import by.mbicycle.develop.weatherappmodule.ui.hourly.HourlyForecastItem
import by.mbicycle.develop.weatherappmodule.ui.hourly.RecyclerAdapterForHourlyTab
import by.mbicycle.develop.weatherappmodule.ui.hourly.RetrofitManagerForHourlyForecast
import java.io.File
import java.util.*

class YesterdayFragment private constructor() : Fragment() {
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
            val files = File(context.filesDir, "/").listFiles()

            files?.let { listOfFiles ->
                if (listOfFiles.isEmpty()) {
                    writeHourlyForecastDataIntoJson(context, currentDateWithPostfix) { result ->
                        if (result) {
                            val files1 = File(context.filesDir, "/").listFiles()
                            JsonHelper.readDataFromJson(context, files1[0].name) { items ->
                                recyclerAdapter.setData(items)
                            }
                        } else {
                            //обработать
                        }
                    }

                } else if (!listOfFiles[0].name.equals(currentDateWithPostfix) && !listOfFiles[0].equals(yesterdayDateWithPostfix)) {
                    listOfFiles[0].delete()
                    writeHourlyForecastDataIntoJson(context, currentDateWithPostfix) { result ->
                        if (result) {
                            val files2 = File(context.filesDir, "/").listFiles()
                            JsonHelper.readDataFromJson(context, files2[0].name) { items ->
                                recyclerAdapter.setData(items)
                            }
                        } else {
                            //обработчик
                        }
                    }
                } else {
                    Handler(Looper.getMainLooper()).post {
                        val files3 = File(context.filesDir, "/").listFiles()
                        JsonHelper.readDataFromJson(context, files3[0].name) { items ->
                            recyclerAdapter.setData(items)
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun writeHourlyForecastDataIntoJson(context: Context, fileName: String, done: (Boolean) -> Unit) {
        val retrofitManager = RetrofitManagerForHourlyForecast()
        val listOfHourlyForecastItems = arrayListOf<HourlyForecastItem>()

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
            retrofitManager.getHourlyForecast(it.latitude, it.longitude) { modelApi ->
                listOfHourlyForecastItems.clear()
                modelApi.listOfHourlyForecast.forEach { hourly ->
                    listOfHourlyForecastItems.add(hourly.mapToItem())
                }

                Handler(Looper.getMainLooper()).post {
                    JsonHelper.exportToJson(context, fileName, listOfHourlyForecastItems, done)
                }
            }
        }
    }

    companion object {
        fun instance() : YesterdayFragment {
            return YesterdayFragment()
        }
    }
}