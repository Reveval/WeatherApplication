package by.mbicycle.develop.weatherappmodule.ui.hourly

import android.content.Context
import by.mbicycle.develop.weatherappmodule.HOURLY_PREFS_FILE_NAME

class PreferencesForHourlyTab private constructor(appContext: Context) {
    private val sharedPreferences = appContext.getSharedPreferences(HOURLY_PREFS_FILE_NAME,
        Context.MODE_PRIVATE)

    fun saveData(key: HourlyTabsPrefsKeys, data: String) {
        sharedPreferences.edit().putString(key.name, data).apply()
    }

    fun loadData(key: HourlyTabsPrefsKeys) : String {
        return sharedPreferences.getString(key.name, "").toString()
    }

    fun eraseData() {
        sharedPreferences.edit().clear().apply()
    }

    fun preferencesIsEmpty() : Boolean {
        HourlyTabsPrefsKeys.values().forEach { key ->
            if (sharedPreferences.contains(key.name) && loadData(key) != "") return false
        }
        return true
    }

    companion object {
        fun instance(appContext: Context) : PreferencesForHourlyTab {
            return PreferencesForHourlyTab(appContext)
        }
    }
}