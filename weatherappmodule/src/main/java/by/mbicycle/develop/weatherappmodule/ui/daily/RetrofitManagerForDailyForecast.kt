package by.mbicycle.develop.weatherappmodule.ui.daily

import android.content.Context
import by.mbicycle.develop.weatherappmodule.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class RetrofitManagerForDailyForecast(context: Context) {
    private val client = OkHttpClient.Builder().apply {
        addInterceptor(HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else
                HttpLoggingInterceptor.Level.NONE
        ))

        addInterceptor(NetworkConnectionInterceptor(context))
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
                block(DailyForecastModelForAPI())
            }
        }
    }
}