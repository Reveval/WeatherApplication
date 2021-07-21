package by.mbicycle.develop.weatherappmodule.ui.city.retrofit

import android.content.Context
import by.mbicycle.develop.weatherappmodule.BASE_URL_FOR_OPEN_WEATHER_API
import by.mbicycle.develop.weatherappmodule.BuildConfig
import by.mbicycle.develop.weatherappmodule.NetworkConnectionInterceptor
import by.mbicycle.develop.weatherappmodule.ui.city.api.WeatherApiForFixedList
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherModelForFixedList
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class RetrofitManagerForFixedList(context: Context) {
    private val client = OkHttpClient.Builder().apply {
        addInterceptor(
            HttpLoggingInterceptor().setLevel(
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

    private val weatherAPI = retrofit.create(WeatherApiForFixedList::class.java)

    fun getDataForFixedList(citiesIDs: ArrayList<Int>, block: (List<WeatherModelForFixedList>) -> Unit) {
        val resultList = arrayListOf<WeatherModelForFixedList>()
        Executors.newCachedThreadPool().submit {
            citiesIDs.forEach {
                try {
                    val response = weatherAPI.getWeatherForFixedList(it).execute()
                    if (response.isSuccessful) {
                        response.body()?.let { model ->
                            resultList.add(model)
                        } ?: block(emptyList())
                    } else {
                        block(emptyList())
                    }
                } catch (ex: Exception) {
                    block(emptyList())
                }
            }
            block(resultList)
        }
    }
}