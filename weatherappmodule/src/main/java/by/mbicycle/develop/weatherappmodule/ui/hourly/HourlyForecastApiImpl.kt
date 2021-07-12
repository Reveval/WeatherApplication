package by.mbicycle.develop.weatherappmodule.ui.hourly

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HourlyForecastApiImpl (
    @SerializedName("hourly")
    @Expose
    val listOfHourlyForecast: List<HourlyForecast> = emptyList())

data class HourlyForecast(
    @SerializedName("dt")
    @Expose
    val date: Long,
    @SerializedName("temp")
    @Expose
    val temp: Double,
    @SerializedName("weather")
    @Expose
    val weatherDetails: List<Weather>
)

data class Weather(
    @SerializedName("id")
    @Expose
    val weatherID: Int
)