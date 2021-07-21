package by.mbicycle.develop.weatherappmodule

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import by.mbicycle.develop.weatherappmodule.ui.city.models.LocationModel
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherItem
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherModelForFixedList
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherModelForSearchList
import by.mbicycle.develop.weatherappmodule.ui.daily.DailyForecast
import by.mbicycle.develop.weatherappmodule.ui.daily.DailyForecastItem
import by.mbicycle.develop.weatherappmodule.ui.hourly.HourlyForecast
import by.mbicycle.develop.weatherappmodule.ui.hourly.HourlyForecastItem
import java.text.SimpleDateFormat
import java.util.*

fun Context.checkPermissions(
    permissions: Array<String>,
    performIfAllIsGranted: () -> Unit,
    performIfSomeDenied: (Array<String>) -> Unit = {}) {

    val deniedPermissionIds =
        permissions.filter {
            (ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED)
        }

    if (deniedPermissionIds.isEmpty())
        performIfAllIsGranted()
    else
        performIfSomeDenied(deniedPermissionIds.toTypedArray())
}

fun WeatherModelForSearchList.mapToItem(locations: List<LocationModel>) : WeatherItem {
    val cityName = locations.filter { link.contains(it.cityKey) }.first().cityName
    return WeatherItem(cityName, weatherIconIdFormat(weatherIconId),
        temperatureFormat(temperature.metric.value),
        formattedDate(DATE_FORMAT_FOR_CURRENT_FORECAST, epochTime, false))
}

fun WeatherModelForFixedList.mapToItem() : WeatherItem {
    return WeatherItem(cityName, weatherIconIdFormat(weatherDetails.first().weatherId),
        temperatureFormat(main.temperature),
        formattedDate(DATE_FORMAT_FOR_CURRENT_FORECAST, date, false))
}

fun DailyForecast.mapToItem() : DailyForecastItem {
    return DailyForecastItem(
        formattedDate(DATE_FORMAT_FOR_DAILY_FORECAST, date, true),
        temperatureFormat(temp.day), weatherIconIdFormat(weatherDetails[0].weatherID))
}

fun HourlyForecast.mapToItem() : HourlyForecastItem {
    val hourFormatWithPattern = SimpleDateFormat(HOUR_FORMAT_FOR_DAILY_FORECAST, Locale.ENGLISH)
        .format(date * 1000L)

    return HourlyForecastItem(
        formattedDate(DATE_FORMAT_FOR_DAILY_FORECAST, date, true),
        hourFormatWithPattern, temperatureFormat(temp),
        weatherIconIdFormat(weatherDetails[0].weatherID))
}

fun CityNameModel.getCityNameWithFormattedDate() : String {
    return if (cityName.isEmpty()) "" else "$cityName - " + formattedDate(DATE_FORMAT_FOR_DAILY_FORECAST, date, true)
}

private fun formattedDate(format: String, dateInMillis: Long, isPostfixNeed: Boolean) : String {
    val date = Date(dateInMillis * 1000L)

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val calendar = Calendar.Builder().apply {
            setInstant(date)
        }.build()

        SimpleDateFormat(format, Locale.ENGLISH)
            .format(calendar.time) +
                if (isPostfixNeed) postfixFormat(calendar.get(Calendar.DAY_OF_MONTH)) else ""
    } else {
        SimpleDateFormat(format, Locale.ENGLISH).format(date) +
                if (isPostfixNeed) postfixFormat(date.day) else ""
    }
}

private fun postfixFormat(dayOfMonth: Int) : String {
    return when(dayOfMonth) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}

private fun weatherIconIdFormat(weatherId: Int) : Int {
    return when(weatherId) {
        in THUNDERSTORM_ID_RANGE_OW_API, in THUNDERSTORM_ID_RANGE_ACCU_API -> R.drawable.ic_thunder
        in RAIN_ID_RANGE_OW_API, in RAIN_ID_RANGE_ACCU_API -> R.drawable.ic_rainy
        in CLOUDS_ID_RANGE_OW_API, in CLOUDS_ID_RANGE_ACCU_API -> R.drawable.ic_cloudy
        else -> R.drawable.ic_sunny
    }
}

private fun temperatureFormat(temp: Double) : String {
    return if (temp < 0.0) {
        "${temp.toInt()} C"
    } else {
        "+ ${temp.toInt()} C"
    }
}