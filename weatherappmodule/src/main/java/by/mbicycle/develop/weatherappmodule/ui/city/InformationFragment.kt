package by.mbicycle.develop.weatherappmodule.ui.city

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import by.mbicycle.develop.weatherappmodule.*
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            if (it is BottomBarVisibilityListener) {
                it.setBottomBarVisibility(View.GONE)
            }
        }

        setFragmentResultListener(REQUEST_KEY) { _, bundle ->
            val result = bundle.getParcelable<WeatherItem>("data")
            result?.let {
                setSelectedItem(it)
            }
        }

        binding.buttonBackStack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setSelectedItem(item: WeatherItem) {
        binding.apply {
            cityName.text = item.cityName
            temperatureTextView.text = item.temperature
            dateTextView.text = item.date
            weatherIconImageView.setImageResource(item.weatherIcon)
        }
    }
}