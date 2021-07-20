package by.mbicycle.develop.weatherappmodule.ui.city.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.mbicycle.develop.weatherappmodule.databinding.ListOfFixedCitiesWeatherBinding
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherItem

class RecyclerAdapterForFixedList :
    RecyclerView.Adapter<RecyclerAdapterForFixedList.ViewHolderForFixedList>() {

    private var inCities = mutableListOf<WeatherItem>()
    private lateinit var binding: ListOfFixedCitiesWeatherBinding
    var itemClickListener: ((position: Int, data: WeatherItem) -> Unit)? = null

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
        holderForFixedList.apply {
            inCities[position].let { item ->
                cityName.text = item.cityName
                temperature.text = item.temperature
                weatherIcon.setImageResource(item.weatherIcon)
                itemView.setOnClickListener { itemClickListener?.invoke(position, item) }
            }
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