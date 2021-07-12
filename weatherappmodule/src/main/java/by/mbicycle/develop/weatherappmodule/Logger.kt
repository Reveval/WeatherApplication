package by.mbicycle.develop.weatherappmodule

import android.util.Log

class Logger {
    companion object {
        fun log(message: String) {
            Log.d(LOG_TAG, message)
        }
    }
}