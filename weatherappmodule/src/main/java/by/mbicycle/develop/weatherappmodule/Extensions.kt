package by.mbicycle.develop.weatherappmodule

import by.mbicycle.develop.weatherappmodule.ui.city.models.LocationModel
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherItem
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherModelForFixedList
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherModelForSearchList
import java.text.SimpleDateFormat
import java.util.*

fun WeatherModelForSearchList.mapToItem(locations: List<LocationModel>) : WeatherItem {
    val cityName = locations.filter { link.contains(it.cityKey) }.first().cityName
    val date = Date(epochTime * 1000L)
    val dateWithPattern = SimpleDateFormat(PATTERN_FOR_DATE_FORMAT, Locale.ENGLISH).format(date)
    return WeatherItem(cityName, weatherIconId, temperature.metric.value.toInt(), dateWithPattern)
}

fun WeatherModelForFixedList.mapToItem() : WeatherItem {
    val date = Date(date * 1000L)
    val dateWithPatten = SimpleDateFormat(PATTERN_FOR_DATE_FORMAT, Locale.ENGLISH).format(date)
    return WeatherItem(cityName, weatherDetails.first().weatherId, main.temperature.toInt(),
        dateWithPatten)
}