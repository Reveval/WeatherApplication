package by.mbicycle.develop.weatherappmodule

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log

@SuppressLint("MissingPermission")
class CurrentLocation(context: Context) : LocationListener {
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    init {
        locationManager.apply {
            if (isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L,
                    0F, this@CurrentLocation)
            }
        }
    }

    fun getLastKnownLocation() : Location? {
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    }

    override fun onLocationChanged(location: Location) {
        Log.d(LOG_TAG, "lat=${location.latitude}, lon=${location.longitude}")
        locationManager.removeUpdates(this)
    }
}