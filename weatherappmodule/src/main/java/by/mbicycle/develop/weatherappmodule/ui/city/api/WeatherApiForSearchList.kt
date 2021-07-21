package by.mbicycle.develop.weatherappmodule.ui.city.api

import by.mbicycle.develop.weatherappmodule.ui.city.models.LocationModel
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherModelForSearchList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val API_KEY_FIRST_ACCOUNT = "fkoHc4a2AJVylA2XW4EABagXNRi2O8XQ"
private const val API_KEY_SECOND_ACCOUNT = "4j3dNwmrGKb5EGJU84p9wBhSOicWdcEk"
private const val API_KEY_THIRD_ACCOUNT = "38YmqVqFSSGqlVANilAf6GAxnRri0F5R"

interface WeatherApiForSearchList {
    @GET("locations/v1/cities/search?apikey=$API_KEY_FIRST_ACCOUNT")
    fun getLocation(@Query("q") searchText: String) : Call<List<LocationModel>>

    @GET("currentconditions/v1/{key}?apikey=$API_KEY_FIRST_ACCOUNT")
    fun getWeatherForSearchList(@Path("key") cityKey: String) :
            Call<List<WeatherModelForSearchList>>
}