package by.mbicycle.develop.weatherappmodule.ui.hourly.yesterday

import android.content.Context
import by.mbicycle.develop.weatherappmodule.ui.hourly.HourlyForecastItem
import com.google.gson.Gson
import java.io.*
import java.util.concurrent.Executors

class JsonHelper {
    companion object {
        fun exportToJson(context: Context, fileName: String, dataList: List<HourlyForecastItem>,
                         writeCompletedFun: (Boolean) -> Unit) {
            Executors.newCachedThreadPool().submit {
                var result = false

                val dataItems = DataItems()
                dataItems.items.addAll(dataList)
                val jsonString = Gson().toJson(dataItems)

                val fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)

                try {
                    fileOutputStream.write(jsonString.toByteArray())
                    result = true
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    result = false
                } finally {
                    fileOutputStream.flush()
                    fileOutputStream.close()
                    writeCompletedFun(result)
                }
            }
        }

        fun readDataFromJson(context: Context, fileName: String, block: (List<HourlyForecastItem>) -> Unit) {
            Executors.newCachedThreadPool().submit {
                val fileInputStream = context.openFileInput(fileName)
                val streamReader = InputStreamReader(fileInputStream)

                try {
                    block(Gson().fromJson(streamReader, DataItems::class.java).items)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    block(emptyList())
                } finally {
                    streamReader.close()
                    fileInputStream.close()
                }
            }
        }
    }

    private class DataItems {
        val items = arrayListOf<HourlyForecastItem>()
    }
}