package by.mbicycle.develop.weatherappmodule.ui.city.retrofit

import android.util.Log
import by.mbicycle.develop.weatherappmodule.BASE_URL_FOR_OPEN_WEATHER_API
import by.mbicycle.develop.weatherappmodule.LOG_TAG
import by.mbicycle.develop.weatherappmodule.ui.city.api.WeatherApiForFixedList
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherModelForFixedList
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class RetrofitManagerForFixedList {
    private val retrofit = Retrofit.Builder().apply {
        baseUrl(BASE_URL_FOR_OPEN_WEATHER_API)
        addConverterFactory(GsonConverterFactory.create())
    }.build()

    private val weatherAPI = retrofit.create(WeatherApiForFixedList::class.java)

    fun getDataForFixedList(citiesIDs: ArrayList<Int>, block: (List<WeatherModelForFixedList>) -> Unit) {
        val resultList = arrayListOf<WeatherModelForFixedList>()
        Executors.newCachedThreadPool().submit {
            citiesIDs.forEach {
                val response = weatherAPI.getWeatherForFixedList(it).execute()
                if (response.isSuccessful) {
                    response.body()?.let { model ->
                        resultList.add(model)
                    } ?: block(emptyList())
                } else {
                    Log.d(LOG_TAG, "code: ${response.code()}")
                    block(emptyList())
                }
            }
            block(resultList)
        }
    }
}