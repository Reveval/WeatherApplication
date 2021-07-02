package by.mbicycle.develop.weatherappmodule.ui.city.api

import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherModelForFixedList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiForFixedList {
    @GET("weather?units=metric&appid=ae1d7412fa4758a0fc38da4d2040c40b")
    fun getWeatherForFixedList(@Query("id") cityID: Int) : Call<WeatherModelForFixedList>
}