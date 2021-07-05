package by.mbicycle.develop.weatherappmodule.ui.city.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.mbicycle.develop.weatherappmodule.R
import by.mbicycle.develop.weatherappmodule.CLOUDS_ID_RANGE_OW_API
import by.mbicycle.develop.weatherappmodule.RAIN_ID_RANGE_OW_API
import by.mbicycle.develop.weatherappmodule.THUNDERSTORM_ID_RANGE_OW_API
import by.mbicycle.develop.weatherappmodule.databinding.ListOfFixedCitiesWeatherBinding
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherItem
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherModelForFixedList

class RecyclerAdapterForFixedList :
    RecyclerView.Adapter<RecyclerAdapterForFixedList.ViewHolderForFixedList>() {

    private var inCities = mutableListOf<WeatherItem>()
    private lateinit var binding: ListOfFixedCitiesWeatherBinding

    fun setWeatherData(citiesTempo: List<WeatherItem>) {
        inCities.apply {
            clear()
            addAll(citiesTempo)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderForFixedList {
        binding = ListOfFixedCitiesWeatherBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolderForFixedList(binding.root)
    }

    override fun onBindViewHolder(holderForFixedList: ViewHolderForFixedList, position: Int) {
        val city = inCities[position]
        val weatherIconId = when(city.weatherIcon) {
            in THUNDERSTORM_ID_RANGE_OW_API -> R.drawable.icon_thunder
            in RAIN_ID_RANGE_OW_API -> R.drawable.icon_rainy
            in CLOUDS_ID_RANGE_OW_API -> R.drawable.icon_cloudy
            else -> R.drawable.icon_sunny
        }
        holderForFixedList.apply {
            cityName.text = city.cityName
            temperature.text = "+${city.temperature} C"
            weatherIcon.setImageResource(weatherIconId)
        }
    }

    override fun getItemCount(): Int {
        return inCities.size
    }

    inner class ViewHolderForFixedList(view: View) : RecyclerView.ViewHolder(view) {
        val cityName = binding.cityTitleTextView
        val temperature = binding.temperatureTextView
        val weatherIcon = binding.weatherIconImageView
    }
}