package by.mbicycle.develop.weatherappmodule.ui.city.api

import by.mbicycle.develop.weatherappmodule.ui.city.models.LocationModel
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherModelForSearchList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApiForSearchList {
    @GET("locations/v1/cities/search?apikey=4j3dNwmrGKb5EGJU84p9wBhSOicWdcEk")
    fun getLocation(@Query("q") searchText: String) : Call<List<LocationModel>>

    @GET("currentconditions/v1/{key}?apikey=4j3dNwmrGKb5EGJU84p9wBhSOicWdcEk")
    fun getWeatherForSearchList(@Path("key") cityKey: String) :
            Call<List<WeatherModelForSearchList>>
}