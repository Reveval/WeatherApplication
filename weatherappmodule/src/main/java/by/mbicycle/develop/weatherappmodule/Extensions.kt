package by.mbicycle.develop.weatherappmodule

import by.mbicycle.develop.weatherappmodule.ui.city.models.LocationModel
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherItem
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherModelForFixedList
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherModelForSearchList

fun WeatherModelForSearchList.mapToItem(locations: List<LocationModel>) : WeatherItem {
    val cityName = locations.filter { link.contains(it.cityKey) }.first().cityName
    return WeatherItem(cityName, weatherIconId, temperature.metric.value.toInt())
}

fun WeatherModelForFixedList.mapToItem() : WeatherItem {
    return WeatherItem(cityName, weatherDetails.first().weatherId, main.temperature.toInt())
}