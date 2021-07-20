package by.mbicycle.develop.weatherappmodule

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
class LocationController(val context: Context) {
    private val defaultLocation = Location("").apply {
        latitude = 0.0
        longitude = 0.0
    }

    @SuppressLint("RestrictedApi")
    private val locationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 1000L
        smallestDisplacement = 1.0F
    }

    fun isGpsAvailable() : Boolean {
        return (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).
            isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun requestLocationUpdate(locationUpdate: (Location) -> Unit) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult?) {
                result?.locations?.forEach { location ->
                    locationUpdate(location)
                } ?: locationUpdate(defaultLocation)
            }
        }

        LocationServices.getFusedLocationProviderClient(context).apply {
            lastLocation.addOnSuccessListener { loc ->
                loc?.let { locationUpdate(loc) } ?: requestLocationUpdates(locationRequest,
                    locationCallback, Looper.getMainLooper())
            }
        }
    }
}