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

                Handler(Looper.getMainLooper()).postDelayed({
                    if (isRefreshing) isRefreshing = false
                }, 1000L)
            }
        }

        showCurrentForecast()
    }

    private fun showCurrentForecast() {
        val retrofitManagerForFixedList = context?.let { RetrofitManagerForFixedList(it) } ?:
            RetrofitManagerForFixedList((requireContext()))
        val retrofitManagerForSearchList = context?.let { RetrofitManagerForSearchList(it) } ?:
            RetrofitManagerForSearchList((requireContext()))
        val groupMessageNoData = binding.groupMessageNoData
        val searchResult = binding.searchResultsTextView

        retrofitManagerForFixedList.let {
            it.getDataForFixedList(citiesIDs) { modelsList ->
                weatherItems.clear()
                modelsList.forEach { model -> weatherItems.add(model.mapToItem()) }

                Handler(Looper.getMainLooper()).post {
                    recyclerAdapterForFixedList.setWeatherData(weatherItems)
                }
            }
        }

        ThrottledSearch(object : ThrottledSearch.Delegate {
            override fun onThrottledSearch(searchTerm: String) {
                weatherItems.clear()

                if (searchTerm.isEmpty()) {
                    activity?.runOnUiThread {
                        recyclerViewForFixedList.visibility = View.VISIBLE
                        searchResult.visibility = View.GONE
                        recyclerViewForSearchList.visibility = View.GONE
                        groupMessageNoData.visibility = View.GONE
                    }
                    return
                }

                retrofitManagerForSearchList.getLocationForSearchList(searchTerm) { locations ->
                    if (locations.isEmpty()) {
                        recyclerViewForFixedList.visibility = View.GONE
                        recyclerViewForSearchList.visibility = View.GONE
                        searchResult.visibility = View.GONE
                        groupMessageNoData.apply {
                            visibility = View.VISIBLE
                            binding.textNoDataTextView.text = "No data for $searchTerm"
                        }
                    } else {
                        recyclerViewForFixedList.visibility = View.GONE
                        searchResult.visibility = View.VISIBLE
                        recyclerViewForSearchList.visibility = View.VISIBLE
                        groupMessageNoData.visibility = View.GONE
                    }

                    retrofitManagerForSearchList.getWeatherDataForSearchList(locations.map { it.cityKey }) { models ->
                        models.forEach { model ->
                            val item = model.mapToItem(locations)
                            weatherItems.add(item)
                        }

                        Handler(Looper.getMainLooper()).post {
                            recyclerAdapterForSearchList.setWeatherData(weatherItems)
                        }
                    }
                }

            }
        }, 1000L).attachTo(binding.editText)

        recyclerAdapterForSearchList.itemClickListener = { _, data ->
            itemClickedActions(data)
        }

        recyclerAdapterForFixedList.itemClickListener = { _, data ->
            itemClickedActions(data)
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
}