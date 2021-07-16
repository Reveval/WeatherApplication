package by.mbicycle.develop.weatherappmodule

import android.content.Context

class PreferencesManager private constructor(appContext: Context) {
    private val sharedPreferences = appContext.getSharedPreferences(LOC_PREFS_FILE_NAME,
        Context.MODE_PRIVATE)

    fun saveData(key: CoordinatesKeys, data: Float) {
        sharedPreferences.edit().putFloat(key.name, data).apply()
    }

    fun loadData(key: CoordinatesKeys) : Double {
        return sharedPreferences.getFloat(key.name, 0.0F).toDouble()
    }

    fun eraseData() {
        sharedPreferences.edit().clear().apply()
    }

    fun preferencesIsEmpty() : Boolean {
        CoordinatesKeys.values().forEach { key ->
            if (sharedPreferences.contains(key.name) && loadData(key) != 0.0) return false
        }
        return true
    }

    companion object {
        fun instance(appContext: Context) : PreferencesManager {
            return PreferencesManager(appContext)
        }
    }
}