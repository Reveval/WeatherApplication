package by.mbicycle.develop.weatherappmodule.ui.city.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.mbicycle.develop.weatherappmodule.*
import by.mbicycle.develop.weatherappmodule.databinding.ListOfSearchCitiesWeatherBinding
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherItem

class RecyclerAdapterForSearchList :
    RecyclerView.Adapter<RecyclerAdapterForSearchList.ViewHolderForSearchList>() {

    private var weatherList = mutableListOf<WeatherItem>()
    private lateinit var binding: ListOfSearchCitiesWeatherBinding
    var itemClickListener: ((position: Int, data: WeatherItem) -> Unit)? = null

    fun setWeatherData(citiesTempo: List<WeatherItem>) {
        weatherList.apply {
            clear()
            addAll(citiesTempo)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderForSearchList {
        binding = ListOfSearchCitiesWeatherBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolderForSearchList(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolderForSearchList, position: Int) {
        val weather = weatherList[position]
        val weatherIconId = when(weather.weatherIcon) {
            in THUNDERSTORM_ID_RANGE_ACCU_API -> R.drawable.icon_thunder
            in RAIN_ID_RANGE_ACCU_API -> R.drawable.icon_rainy
            in RAIN_ID_RANGE_ACCU_API -> R.drawable.icon_cloudy
            else -> R.drawable.icon_sunny
        }

        holder.apply {
            cityName.text = weather.cityName
            temperature.text = "+${weather.temperature} C"
            iconOfWeather.setImageResource(weatherIconId)
        }

        holder.itemView.setOnClickListener { itemClickListener?.invoke(position, weather) }
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    inner class ViewHolderForSearchList(view: View) : RecyclerView.ViewHolder(view) {
        val cityName = binding.searchCityNameTextView
        val temperature = binding.searchTemperatureTextView
        val iconOfWeather = binding.searchIconOfWeatherImageView
    }
}
