package by.mbicycle.develop.weatherappmodule.ui.city.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LocationModel(
    @SerializedName("Key")
    @Expose
    val cityKey: String,
    @SerializedName("EnglishName")
    @Expose
    val cityName: String
)