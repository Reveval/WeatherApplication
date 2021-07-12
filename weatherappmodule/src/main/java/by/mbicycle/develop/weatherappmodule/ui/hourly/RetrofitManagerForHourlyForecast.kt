package by.mbicycle.develop.weatherappmodule.ui.hourly

import by.mbicycle.develop.weatherappmodule.BASE_URL_FOR_OPEN_WEATHER_API
import by.mbicycle.develop.weatherappmodule.BuildConfig
import by.mbicycle.develop.weatherappmodule.CityNameModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class RetrofitManagerForHourlyForecast {
    private val client = OkHttpClient.Builder().apply {
        addInterceptor(
            HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else
                HttpLoggingInterceptor.Level.NONE
        ))
    }.build()

    private val retrofit = Retrofit.Builder().apply {
        baseUrl(BASE_URL_FOR_OPEN_WEATHER_API)
        addConverterFactory(GsonConverterFactory.create())
        client(client)
    }.build()

    private val hourlyForecastApi = retrofit.create(HourlyForecastApi::class.java)

    fun getCityName(lat: Double, lon: Double, block: (CityNameModel) -> Unit) {
        Executors.newCachedThreadPool().submit {
            val response = hourlyForecastApi.getCityName(lat, lon).execute()
            if (response.isSuccessful) {
                response.body()?.let { block(it) }
            } else {
                block(CityNameModel())
            }
        }
    }

    fun getHourlyForecast(lat: Double, lon: Double, block: (HourlyForecastApiImpl) -> Unit) {
        Executors.newCachedThreadPool().submit {
            val response = hourlyForecastApi.getHourlyForecast(lat, lon).execute()
            if (response.isSuccessful) {
                response.body()?.let { block(it) }
            } else {
                block(HourlyForecastApiImpl())
            }
        }
    }
}