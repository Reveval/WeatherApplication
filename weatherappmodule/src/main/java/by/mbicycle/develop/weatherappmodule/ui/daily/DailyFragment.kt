package by.mbicycle.develop.weatherappmodule.ui.daily

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.mbicycle.develop.weatherappmodule.CurrentLocation
import by.mbicycle.develop.weatherappmodule.databinding.FragmentDailyBinding
import by.mbicycle.develop.weatherappmodule.mapToModel

class DailyFragment : Fragment() {
    private lateinit var binding: FragmentDailyBinding
    private lateinit var retrofitManager: RetrofitManagerForDailyForecast
    private lateinit var recyclerAdapter: RecyclerAdapterForDailyTab
    private var location: Location? = null
    private var listOfDailyForecastItems = arrayListOf<DailyForecastItem>()

    private val requestLocationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            showDailyForecast()
        } else {
            showMessageNoData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retrofitManager = RetrofitManagerForDailyForecast()
    }

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

        listOfDailyForecastItems.clear()

        recyclerAdapter = RecyclerAdapterForDailyTab()

        binding.recyclerForDailyForecast.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = recyclerAdapter
            overScrollMode = View.OVER_SCROLL_NEVER
        }

        context?.let { it ->
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
                showDailyForecast()
            } else {
                requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun getLocationData() : Location? {
        return context?.let { CurrentLocation(it).getLastKnownLocation() }
    }

    private fun showMessageNoData() {
        binding.apply {
            groupMessageNoData.visibility = View.VISIBLE
            buttonAllowAccess.setOnClickListener {
                requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun showDailyForecast() {
        binding.groupMessageNoData.visibility = View.GONE
        location = getLocationData()
        location?.let {

            retrofitManager.getCityName(it.latitude, it.longitude) { model ->

                Handler(Looper.getMainLooper()).post {
                    binding.cityNameTextView.text = model.cityName
                }
            }

            retrofitManager.getDailyForecast(it.latitude, it.longitude) { modelApi ->
                modelApi.listOfDailyForecast.forEach { daily ->
                    val item = daily.mapToModel()
                    listOfDailyForecastItems.add(item)
                }

                Handler(Looper.getMainLooper()).post {
                    recyclerAdapter.setData(listOfDailyForecastItems)
                }
            }
        }
    }
}