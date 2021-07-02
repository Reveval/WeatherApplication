package by.mbicycle.develop.weatherappmodule.ui.city

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.mbicycle.develop.weatherappmodule.BottomBarVisibilityListener
import by.mbicycle.develop.weatherappmodule.databinding.InformationFragmentBinding
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherItem

class InformationFragment : Fragment() {
    private lateinit var binding: InformationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InformationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            if (it is BottomBarVisibilityListener) {
                it.setBottomBarVisibility(View.GONE)
            }
        }
    }

    fun setSelectedItem(item: WeatherItem) {
        binding.apply {
            cityName.text = item.cityName
            temperatureTextView.text = item.temperature.toString()
        }
    }
}