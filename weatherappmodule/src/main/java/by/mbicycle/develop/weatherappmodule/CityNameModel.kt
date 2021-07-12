package by.mbicycle.develop.weatherappmodule

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CityNameModel(
    @SerializedName("name")
    @Expose
    val cityName: String = ""
)