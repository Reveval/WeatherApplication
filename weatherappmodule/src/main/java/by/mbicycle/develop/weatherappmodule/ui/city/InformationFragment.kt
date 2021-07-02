package by.mbicycle.develop.weatherappmodule.ui.city

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.mbicycle.develop.weatherappmodule.databinding.FragmentInformationBinding
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherItem

class InformationFragment : Fragment() {
    private lateinit var binding: FragmentInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun setSelectedItem(item: WeatherItem) {
        binding.apply {
            cityName.text = item.cityName
            temperatureTextView.text = item.temperature.toString()
        }
    }
}