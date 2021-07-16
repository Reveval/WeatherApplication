package by.mbicycle.develop.weatherappmodule

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
class LocationController(context: Context, locationUpdate: (Location) -> Unit) {
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

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            result?.locations?.forEach { location ->
                locationUpdate(location)
            } ?: locationUpdate(defaultLocation)
        }
    }

    init {
        LocationServices.getFusedLocationProviderClient(context).apply {
            lastLocation.addOnSuccessListener { loc ->
                loc?.let { locationUpdate(loc) } ?: requestLocationUpdates(locationRequest,
                    locationCallback, Looper.getMainLooper())
            }
        }
    }
}