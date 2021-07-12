package by.mbicycle.develop.weatherappmodule.ui.daily

import android.util.Log
import by.mbicycle.develop.weatherappmodule.BASE_URL_FOR_OPEN_WEATHER_API
import by.mbicycle.develop.weatherappmodule.BuildConfig
import by.mbicycle.develop.weatherappmodule.LOG_TAG
import by.mbicycle.develop.weatherappmodule.CityNameModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class RetrofitManagerForDailyForecast {
    private val client = OkHttpClient.Builder().apply {
        addInterceptor(HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else
                HttpLoggingInterceptor.Level.NONE
        ))
    }.build()

    private val retrofit = Retrofit.Builder().apply {
        baseUrl(BASE_URL_FOR_OPEN_WEATHER_API)
        addConverterFactory(GsonConverterFactory.create())
        client(client)
    }.build()

    private val dailyForecastApi = retrofit.create(DailyForecastApi::class.java)

    fun getCityName(lat: Double, lon: Double, block: (CityNameModel) -> Unit) {
        Executors.newCachedThreadPool().submit {
            val response = dailyForecastApi.getCityName(lat, lon).execute()
            if (response.isSuccessful) {
                response.body()?.let { block(it) }
            } else {
                block(CityNameModel())
            }
        }
    }

    fun getDailyForecast(lat: Double, lon: Double, block: (DailyForecastModelForAPI) -> Unit) {
        Executors.newCachedThreadPool().submit {
            val response = dailyForecastApi.getDailyForecast(lat, lon).execute()
            if (response.isSuccessful) {
                response.body()?.let { block(it) }
            } else {
                Log.d(LOG_TAG, "code: ${response.code()}")
                block(DailyForecastModelForAPI())
            }
        }
    }
}