package by.mbicycle.develop.weatherappmodule.ui.city.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherModelForSearchList (
    @SerializedName("WeatherIcon")
    @Expose
    val weatherIconId: Int,
    @SerializedName("Temperature")
    @Expose
    val temperature: Temperature,
    @SerializedName("Link")
    @Expose
    val link: String,
    @SerializedName("EpochTime")
    @Expose
    val epochTime: Long
)

data class Temperature(
    @SerializedName("Metric")
    @Expose
    val metric: Metric
)

data class Metric(
    @SerializedName("Value")
    @Expose
    val value: Double
)