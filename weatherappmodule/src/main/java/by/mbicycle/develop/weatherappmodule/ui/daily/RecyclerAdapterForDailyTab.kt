package by.mbicycle.develop.weatherappmodule.ui.daily

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.mbicycle.develop.weatherappmodule.databinding.ListOfDailyForecastBinding

class RecyclerAdapterForDailyTab :
    RecyclerView.Adapter<RecyclerAdapterForDailyTab.ViewHolderForDailyTab>() {

    private lateinit var binding: ListOfDailyForecastBinding
    private var inListOfDailyForecastItems = mutableListOf<DailyForecastItem>()

    fun setData(listOfDailyForecastItems: List<DailyForecastItem>) {
        inListOfDailyForecastItems.apply {
            clear()
            addAll(listOfDailyForecastItems)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderForDailyTab {
        binding = ListOfDailyForecastBinding.inflate(LayoutInflater.from(parent.context), parent,
            false)
        return ViewHolderForDailyTab(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolderForDailyTab, position: Int) {
        val item = inListOfDailyForecastItems[position]
        holder.apply {
            date.text = item.date
            temperature.text = item.temperature
            iconOfWeather.setImageDrawable(iconOfWeather.resources.getDrawable(item.weatherId))
        }
    }

    override fun getItemCount(): Int {
        return inListOfDailyForecastItems.size
    }

    inner class ViewHolderForDailyTab(view: View) : RecyclerView.ViewHolder(view) {
        val date = binding.dailyDateTextView
        val temperature = binding.dailyTemperatureTextView
        val iconOfWeather = binding.dailyIconOfWeatherImageView
    }
}