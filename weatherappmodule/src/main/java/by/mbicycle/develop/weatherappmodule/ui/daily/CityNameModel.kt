package by.mbicycle.develop.weatherappmodule.ui.daily

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CityNameModel(
    @SerializedName("name")
    @Expose
    val cityName: String
) {
    companion object {
        fun emptyInstance() : CityNameModel {
            return CityNameModel("")
        }
    }
}