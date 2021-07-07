package by.mbicycle.develop.weatherappmodule.ui.daily

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DailyForecastModelForAPI (
    @SerializedName("daily")
    @Expose
    val listOfDailyForecast: List<DailyForecast>
) {
    companion object {
        fun emptyInstance() : DailyForecastModelForAPI {
            return DailyForecastModelForAPI(emptyList())
        }
    }
}

data class DailyForecast(
    @SerializedName("dt")
    @Expose
    val date: Long,
    @SerializedName("temp")
    @Expose
    val temp: Temp,
    @SerializedName("weather")
    @Expose
    val weatherDetails: List<Weather>
)

data class Temp(
    @SerializedName("day")
    @Expose
    val day: Double
)

data class Weather(
    @SerializedName("id")
    @Expose
    val weatherID: Int
)