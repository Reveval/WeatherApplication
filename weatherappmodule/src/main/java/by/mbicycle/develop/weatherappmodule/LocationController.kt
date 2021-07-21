package by.mbicycle.develop.weatherappmodule

import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.location.Location
import android.os.Looper
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices

const val REQUEST_CHECK_LOCATION_SETTINGS = 9738

@SuppressLint("MissingPermission")
class LocationController(val activity: Activity) {
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

    fun checkLocationSettings(isGpsAvailable: (Boolean) -> Unit) {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(activity)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            isGpsAvailable(true)
            Logger.log("All location settings are satisfied")
        }

        task.addOnFailureListener { exception ->
            isGpsAvailable(false)
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(activity, REQUEST_CHECK_LOCATION_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Logger.log(sendEx.message.toString())
                }
            }
        }
    }

    fun requestLocationUpdate(locationUpdate: (Location) -> Unit) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult?) {
                result?.locations?.forEach { location ->
                    locationUpdate(location)
                } ?: locationUpdate(defaultLocation)
            }
        }

        LocationServices.getFusedLocationProviderClient(activity).apply {
            lastLocation.addOnSuccessListener { loc ->
                loc?.let { locationUpdate(loc) } ?: requestLocationUpdates(locationRequest,
                    locationCallback, Looper.getMainLooper())
            }
        }
    }
}