package by.mbicycle.develop.weatherappmodule.ui.city

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.mbicycle.develop.weatherappmodule.*
import by.mbicycle.develop.weatherappmodule.databinding.FragmentCityBinding
import by.mbicycle.develop.weatherappmodule.ui.city.adapters.RecyclerAdapterForFixedList
import by.mbicycle.develop.weatherappmodule.ui.city.adapters.RecyclerAdapterForSearchList
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherItem
import by.mbicycle.develop.weatherappmodule.ui.city.retrofit.RetrofitManagerForFixedList
import by.mbicycle.develop.weatherappmodule.ui.city.retrofit.RetrofitManagerForSearchList

class CityFragment : Fragment(), UpdateDataListener {
    private lateinit var binding: FragmentCityBinding
    private lateinit var recyclerViewForFixedList: RecyclerView
    private lateinit var recyclerViewForSearchList: RecyclerView
    private val recyclerAdapterForFixedList = RecyclerAdapterForFixedList()
    private val recyclerAdapterForSearchList = RecyclerAdapterForSearchList()
    private val citiesIDs = arrayListOf(HOMYEL_ID, MINSK_ID, NEW_YORK_ID, TOKYO_ID, LONDON_ID,
        PARIS_ID, SYDNEY_ID, LOS_ANGELES_ID)
    private val weatherItems = arrayListOf<WeatherItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            if (it is BottomBarVisibilityListener) {
                it.setBottomBarVisibility(View.VISIBLE)
            }
        }

        recyclerViewForFixedList = binding.recyclerOfFixedCitiesWeathers.apply {
            layoutManager = GridLayoutManager(activity, 2)
            adapter = recyclerAdapterForFixedList
            overScrollMode = View.OVER_SCROLL_NEVER
        }

        recyclerViewForSearchList = binding.recyclerOfSearchCitiesWeathers.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = recyclerAdapterForSearchList
            overScrollMode = View.OVER_SCROLL_NEVER
        }

        binding.citySwipeRefresh.apply {
            setOnRefreshListener {
                activity?.let {
                    if(it is SwipeRefreshListener) {
                        it.updateData()
                    }
                }

                if (isRefreshing) isRefreshing = false
            }
        }

        showCurrentForecast()
    }

    private fun showCurrentForecast() {
        val retrofitManagerForFixedList = context?.let { RetrofitManagerForFixedList(it) } ?:
            RetrofitManagerForFixedList((requireContext()))
        val retrofitManagerForSearchList = context?.let { RetrofitManagerForSearchList(it) } ?:
            RetrofitManagerForSearchList((requireContext()))

        changeProgressBarVisibility(View.VISIBLE)

        retrofitManagerForFixedList.let {
            it.getDataForFixedList(citiesIDs) { modelsList ->

                isNeedToShowMessageNoUpdate(modelsList.isEmpty())

                weatherItems.clear()
                modelsList.forEach { model -> weatherItems.add(model.mapToItem()) }

                Handler(Looper.getMainLooper()).post {
                    changeProgressBarVisibility(View.GONE)
                    recyclerAdapterForFixedList.setWeatherData(weatherItems)
                }
            }
        }

        ThrottledSearch(object : ThrottledSearch.Delegate {
            override fun onThrottledSearch(searchTerm: String) {
                weatherItems.clear()

                if (searchTerm.isEmpty()) {
                    activity?.runOnUiThread {
                        binding.apply {
                            recyclerOfFixedCitiesWeathers.visibility = View.VISIBLE
                            groupViewForSearchList.visibility = View.GONE
                            messageNoDataForCity.root.visibility = View.GONE
                        }
                    }
                    return
                }

                activity?.runOnUiThread {
                    changeProgressBarVisibility(View.VISIBLE)
                }

                retrofitManagerForSearchList.getLocationForSearchList(searchTerm) { locations ->
                    if (locations.isEmpty()) {
                        binding.apply {
                            recyclerOfFixedCitiesWeathers.visibility = View.GONE
                            groupViewForSearchList.visibility = View.GONE
                            noUpdateForCityTab.root.visibility = View.GONE
                            messageNoDataForCity.let {
                                it.root.visibility = View.VISIBLE
                                it.textNoDataTextView.text = "No data for $searchTerm"
                            }
                        }
                    } else {
                        binding.apply {
                            recyclerOfFixedCitiesWeathers.visibility = View.GONE
                            groupViewForSearchList.visibility = View.VISIBLE
                            messageNoDataForCity.root.visibility = View.GONE
                        }
                    }

                    retrofitManagerForSearchList.getWeatherDataForSearchList(locations.map { it.cityKey }) { models ->
                        weatherItems.clear()
                        models.forEach { model ->
                            val item = model.mapToItem(locations)
                            weatherItems.add(item)
                        }

                        Handler(Looper.getMainLooper()).post {

                            activity?.runOnUiThread {
                                changeProgressBarVisibility(View.GONE)
                            }

                            recyclerAdapterForSearchList.setWeatherData(weatherItems)
                        }
                    }
                }

            }
        }, 3000L).attachTo(binding.editText)

        recyclerAdapterForSearchList.itemClickListener = { _, data ->
            itemClickedActions(data)
        }

        recyclerAdapterForFixedList.itemClickListener = { _, data ->
            itemClickedActions(data)
        }
    }

    private fun isNeedToShowMessageNoUpdate(predicate: Boolean) {
        Handler(Looper.getMainLooper()).post {
            if (predicate) {
                binding.apply {
                    changeProgressBarVisibility(View.GONE)
                    noUpdateForCityTab.root.visibility = View.VISIBLE
                }
            } else {
                binding.noUpdateForCityTab.root.visibility = View.GONE
            }
        }
    }

    private fun itemClickedActions(data: WeatherItem) {
        parentFragmentManager.commit {
            replace<InformationFragment>(R.id.root_city_fragment_container)
            setFragmentResult(REQUEST_KEY, bundleOf("data" to data))
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    override fun reloadData() {
        showCurrentForecast()
    }

    private fun changeProgressBarVisibility(visibility: Int) {
        activity?.let {
            if (it is ProgressBarVisibilityListener) {
                it.setProgressBarVisibility(visibility)
            }
        }
    }
}