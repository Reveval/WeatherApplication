package by.mbicycle.develop.weatherappmodule.ui.city.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherModelForFixedList(
    @SerializedName("main")
    @Expose
    val main: Main,
    @SerializedName("id")
    @Expose
    val cityID: Int,
    @SerializedName("name")
    @Expose
    val cityName: String,
    @SerializedName("weather")
    val weatherDetails: List<Weather>)

data class Main (
    @SerializedName("temp")
    @Expose
    val temperature: Double
)

data class Weather(
    @SerializedName("id")
    @Expose
    val weatherId: Int
)