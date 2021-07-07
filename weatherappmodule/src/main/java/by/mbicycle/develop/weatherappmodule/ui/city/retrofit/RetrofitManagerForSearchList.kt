package by.mbicycle.develop.weatherappmodule.ui.city.retrofit

import android.util.Log
import by.mbicycle.develop.weatherappmodule.BASE_URL_FOR_ACCU_WEATHER_API
import by.mbicycle.develop.weatherappmodule.LOG_TAG
import by.mbicycle.develop.weatherappmodule.ui.city.models.LocationModel
import by.mbicycle.develop.weatherappmodule.ui.city.api.WeatherApiForSearchList
import by.mbicycle.develop.weatherappmodule.ui.city.models.WeatherModelForSearchList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class RetrofitManagerForSearchList {
    private val retrofit = Retrofit.Builder().apply {
        baseUrl(BASE_URL_FOR_ACCU_WEATHER_API)
        addConverterFactory(GsonConverterFactory.create())
    }.build()

    private val weatherAPI = retrofit.create(WeatherApiForSearchList::class.java)

    fun getLocationForSearchList(searchText: String, block: (List<LocationModel>) -> Unit ) {
        val call = weatherAPI.getLocation(searchText)
        call.enqueue(object : Callback<List<LocationModel>> {
            override fun onResponse(
                call: Call<List<LocationModel>>,
                response: Response<List<LocationModel>>
            ) {
                if (response.isSuccessful) {
                    block(
                        response.body()?.let { list ->
                            arrayListOf<LocationModel>().apply {
                                addAll(list)
                            }
                        } ?: emptyList()
                    )
                } else {
                    Log.d(LOG_TAG, "code: ${response.code()}")
                    block(emptyList())
                }
            }

            override fun onFailure(call: Call<List<LocationModel>>, t: Throwable) {
                Log.d(LOG_TAG, "error: $t")
                block(emptyList())
            }
        })
    }

    fun getWeatherDataForSearchList(citiesKeys: List<String>, block:
        (List<WeatherModelForSearchList>) -> Unit) {
        val resultList = arrayListOf<WeatherModelForSearchList>()
        Executors.newCachedThreadPool().submit {
            citiesKeys.forEach {
                val response = weatherAPI.getWeatherForSearchList(it).execute()
                if (response.isSuccessful) {
                    response.body()?.let { list ->
                        resultList.add(list.first())
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