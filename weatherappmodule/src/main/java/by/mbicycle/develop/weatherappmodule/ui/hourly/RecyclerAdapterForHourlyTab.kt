package by.mbicycle.develop.weatherappmodule.ui.hourly

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.mbicycle.develop.weatherappmodule.databinding.ListOfHourlyForecastBinding

class RecyclerAdapterForHourlyTab :
    RecyclerView.Adapter<RecyclerAdapterForHourlyTab.ViewHolderForHourlyTab>() {
    private lateinit var binding: ListOfHourlyForecastBinding
    private var inListOfHourlyForecastItems = mutableListOf<HourlyForecastItem>()

    fun setData(listOfHourlyForecastItems: List<HourlyForecastItem>) {
        inListOfHourlyForecastItems.apply {
            clear()
            addAll(listOfHourlyForecastItems)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderForHourlyTab {
        binding = ListOfHourlyForecastBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolderForHourlyTab(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolderForHourlyTab, position: Int) {
        val item = inListOfHourlyForecastItems[position]
        holder.apply {
            hour.text = item.hour
            temperature.text = item.temperature
            icon.setImageResource(item.weatherId)
        }
    }

    override fun getItemCount(): Int {
        return inListOfHourlyForecastItems.size
    }

    inner class ViewHolderForHourlyTab(view: View) : RecyclerView.ViewHolder(view) {
        val hour = binding.hourlyHourTextView
        val temperature = binding.hourlyTemperatureTextView
        val icon = binding.hourlyIconOfWeatherImageView
    }
}