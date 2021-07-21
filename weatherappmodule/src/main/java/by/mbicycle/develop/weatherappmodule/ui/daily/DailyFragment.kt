package by.mbicycle.develop.weatherappmodule.ui.daily

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
import by.mbicycle.develop.weatherappmodule.databinding.FragmentDailyBinding

class DailyFragment : Fragment(), UpdateDataListener {
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

        binding.dailySwipeRefresh.apply {
            setOnRefreshListener {
                activity?.let {
                    if (it is SwipeRefreshListener) {
                        it.updateData()
                    }
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    if (isRefreshing) isRefreshing = false
                }, 1000L)
            }
        }

        showDailyForecast()
    }

    @SuppressLint("MissingPermission")
    private fun showDailyForecast() {
        val retrofitManager = context?.let { RetrofitManagerForDailyForecast(it) } ?:
            RetrofitManagerForDailyForecast(requireContext())
        val listOfDailyForecastItems = arrayListOf<DailyForecastItem>()

        context?.let { ctx ->
            binding.progressContainer.root.visibility = View.VISIBLE

            PreferencesManager.instance(ctx).let { prefs ->

                isNeedToShowMessageNoUpdate(prefs.preferencesIsEmpty())

                val latitude = prefs.loadData(CoordinatesKeys.LATITUDE)
                val longitude = prefs.loadData(CoordinatesKeys.LONGITUDE)

                retrofitManager.getCityName(latitude, longitude) { model ->
                    isNeedToShowMessageNoUpdate(model.cityName.isEmpty())

                    Handler(Looper.getMainLooper()).post {
                        binding.cityNameTextView.text = model.cityName
                    }
                }

                retrofitManager.getDailyForecast(latitude, longitude) { modelApi ->
                    isNeedToShowMessageNoUpdate(modelApi.listOfDailyForecast.isEmpty())

                    listOfDailyForecastItems.clear()
                    modelApi.listOfDailyForecast.forEach { daily ->
                        listOfDailyForecastItems.add(daily.mapToItem())
                    }

                    Handler(Looper.getMainLooper()).post {
                        binding.progressContainer.root.visibility = View.GONE
                        recyclerAdapter.setData(listOfDailyForecastItems)
                    }
                }
            }
        }
    }

    private fun isNeedToShowMessageNoUpdate(predicate: Boolean) {
        Handler(Looper.getMainLooper()).post {
            if (predicate) {
                binding.apply {
                    progressContainer.root.visibility = View.GONE
                    messageCannotGetUpdateForDaily.root.visibility = View.VISIBLE
                }
            } else {
                binding.messageCannotGetUpdateForDaily.root.visibility = View.GONE
            }
        }
    }

    override fun reloadData() {
        showDailyForecast()
    }
}