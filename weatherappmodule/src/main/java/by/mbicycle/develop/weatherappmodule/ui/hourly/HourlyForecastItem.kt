package by.mbicycle.develop.weatherappmodule.ui.hourly

data class HourlyForecastItem(
    val date: String,
    val hour: String,
    val temperature: String,
    val weatherId: Int
)