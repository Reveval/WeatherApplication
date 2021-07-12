package by.mbicycle.develop.weatherappmodule.ui.hourly

import by.mbicycle.develop.weatherappmodule.CityNameModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface HourlyForecastApi {
    @GET("onecall?units=metric&exclude=current,minutely,daily,alerts&appid=ae1d7412fa4758a0fc38da4d2040c40b")
    fun getHourlyForecast(@Query("lat") lat: Double, @Query("lon") lon: Double) : Call<HourlyForecastApiImpl>

    @GET("weather?units=metric&appid=ae1d7412fa4758a0fc38da4d2040c40b")
    fun getCityName(@Query("lat") lat: Double, @Query("lon") lon: Double) : Call<CityNameModel>
}