package by.mbicycle.develop.weatherappmodule.ui.daily

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
import by.mbicycle.develop.weatherappmodule.databinding.FragmentDailyBinding
import by.mbicycle.develop.weatherappmodule.mapToItem

class DailyFragment : Fragment() {
    private lateinit var binding: FragmentDailyBinding
    private val recyclerAdapter = RecyclerAdapterForDailyTab()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerForDailyForecast.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = recyclerAdapter
            overScrollMode = View.OVER_SCROLL_NEVER
        }

        showDailyForecast()
    }

    @SuppressLint("MissingPermission")
    private fun showDailyForecast() {
        val retrofitManager = RetrofitManagerForDailyForecast()
        val listOfDailyForecastItems = arrayListOf<DailyForecastItem>()

        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
            retrofitManager.getCityName(it.latitude, it.longitude) { model ->

                Handler(Looper.getMainLooper()).post {
                    binding.cityNameTextView.text = model.cityName
                }
            }

            retrofitManager.getDailyForecast(it.latitude, it.longitude) { modelApi ->
                listOfDailyForecastItems.clear()
                modelApi.listOfDailyForecast.forEach { daily ->
                    listOfDailyForecastItems.add(daily.mapToItem())
                }

                Handler(Looper.getMainLooper()).post {
                    recyclerAdapter.setData(listOfDailyForecastItems)
                }
            }
        }
    }
}