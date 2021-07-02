package by.mbicycle.develop.weatherappmodule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.mbicycle.develop.weatherappmodule.databinding.ActivityInfoBinding
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherItem

class InfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.extras?.getParcelable<WeatherItem>(WeatherItem::class.java.simpleName)

        data?.let {
            binding.apply {
                cityName.text = it.cityName
                temperatureTextView.text = it.temperature.toString()
            }
        }
    }
}