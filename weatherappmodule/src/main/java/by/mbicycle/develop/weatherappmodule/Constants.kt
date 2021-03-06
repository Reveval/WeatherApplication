package by.mbicycle.develop.weatherappmodule

const val LOG_TAG = "myLogs"
const val BASE_URL_FOR_FIXED_LIST = "https://api.openweathermap.org/data/2.5/"
const val BASE_URL_FOR_SEARCH_LIST = "https://dataservice.accuweather.com/"

/* CITIES IDENTIFIERS */
const val HOMYEL_ID = 627907
const val LONDON_ID = 2643743
const val NEW_YORK_ID = 5128638
const val TOKYO_ID = 1850147
const val PARIS_ID = 2968815
const val MINSK_ID = 625144
const val LOS_ANGELES_ID = 5368361
const val SYDNEY_ID = 6160752

/* WEATHERS IDENTIFIERS FOR FIXED LIST */
val THUNDERSTORM_ID_RANGE_OW_API = 200..232
val RAIN_ID_RANGE_OW_API = 500..531
val CLOUDS_ID_RANGE_OW_API = 801..804

/* WEATHERS IDENTIFIERS FOR SEARCH LIST */
val THUNDERSTORM_ID_RANGE_ACCU_API = 12..17
val RAIN_ID_RANGE_ACCU_API = 18..21
val CLOUDS_ID_RANGE_ACCU_API = 4..11