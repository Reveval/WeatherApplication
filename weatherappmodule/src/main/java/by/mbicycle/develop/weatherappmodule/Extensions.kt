package by.mbicycle.develop.weatherappmodule

import android.os.Build
import by.mbicycle.develop.weatherappmodule.ui.city.models.LocationModel
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherItem
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherModelForFixedList
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherModelForSearchList
import by.mbicycle.develop.weatherappmodule.ui.daily.DailyForecast
import by.mbicycle.develop.weatherappmodule.ui.daily.DailyForecastItem
import java.text.SimpleDateFormat
import java.util.*

fun WeatherModelForSearchList.mapToItem(locations: List<LocationModel>) : WeatherItem {
    val cityName = locations.filter { link.contains(it.cityKey) }.first().cityName
    val date = Date(epochTime * 1000L)
    val dateWithPattern = SimpleDateFormat(DATE_FORMAT_FOR_CURRENT_FORECAST, Locale.ENGLISH).format(date)
    return WeatherItem(cityName, weatherIconId, temperature.metric.value.toInt(), dateWithPattern)
}

fun WeatherModelForFixedList.mapToItem() : WeatherItem {
    val date = Date(date * 1000L)
    val dateWithPatten = SimpleDateFormat(DATE_FORMAT_FOR_CURRENT_FORECAST, Locale.ENGLISH).format(date)
    return WeatherItem(cityName, weatherDetails.first().weatherId, main.temperature.toInt(),
        dateWithPatten)
}

fun DailyForecast.mapToModel() : DailyForecastItem {
    val date = Date(date * 1000L)

    val dateWithPattern = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val calendar = Calendar.Builder().apply {
            setInstant(date)
        }.build()

        val postfix = when(calendar.get(Calendar.DAY_OF_MONTH)) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }

        SimpleDateFormat(DATE_FORMAT_FOR_DAILY_FORECAST, Locale.ENGLISH)
            .format(calendar.time) + postfix
    } else {
        SimpleDateFormat(DATE_FORMAT_FOR_CURRENT_FORECAST, Locale.ENGLISH).format(date)
    }

    return DailyForecastItem(dateWithPattern, chooseTemperature(temp.day),
        chooseWeatherIconID(weatherDetails[0].weatherID))
}

private fun chooseWeatherIconID(weatherId: Int) : Int {
    return when(weatherId) {
        in THUNDERSTORM_ID_RANGE_OW_API, in THUNDERSTORM_ID_RANGE_ACCU_API -> R.drawable.icon_thunder
        in RAIN_ID_RANGE_OW_API, in RAIN_ID_RANGE_ACCU_API -> R.drawable.icon_rainy
        in CLOUDS_ID_RANGE_OW_API, in CLOUDS_ID_RANGE_ACCU_API -> R.drawable.icon_cloudy
        else -> R.drawable.icon_sunny
    }
}

private fun chooseTemperature(temp: Double) : String {
    return if (temp < 0.0) {
        "${temp.toInt()} C"
    } else {
        "+ ${temp.toInt()} C"
    }
}