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
        val weatherIconID = when(item.weatherIcon) {
            in THUNDERSTORM_ID_RANGE_OW_API, in THUNDERSTORM_ID_RANGE_ACCU_API -> R.drawable.icon_thunder
            in RAIN_ID_RANGE_OW_API, in RAIN_ID_RANGE_ACCU_API -> R.drawable.icon_rainy
            in CLOUDS_ID_RANGE_OW_API, in CLOUDS_ID_RANGE_ACCU_API -> R.drawable.icon_cloudy
            else -> R.drawable.icon_sunny
        }

        binding.apply {
            cityName.text = item.cityName
            temperatureTextView.text = "+${item.temperature} C"
            dateTextView.text = item.date
            weatherIconImageView.setImageResource(weatherIconID)
        }
    }
}