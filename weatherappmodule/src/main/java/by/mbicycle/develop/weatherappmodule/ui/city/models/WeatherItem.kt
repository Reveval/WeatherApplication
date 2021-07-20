package by.mbicycle.develop.weatherappmodule.ui.city.models

import android.os.Parcel
import android.os.Parcelable

data class WeatherItem(
    val cityName: String,
    val weatherIcon: Int,
    val temperature: String,
    val date: String)
    : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cityName)
        parcel.writeInt(weatherIcon)
        parcel.writeString(temperature)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeatherItem> {
        override fun createFromParcel(parcel: Parcel): WeatherItem {
            return WeatherItem(parcel)
        }

        override fun newArray(size: Int): Array<WeatherItem?> {
            return arrayOfNulls(size)
        }
    }
}