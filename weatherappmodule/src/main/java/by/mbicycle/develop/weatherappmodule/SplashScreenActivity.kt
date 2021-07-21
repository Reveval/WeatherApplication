package by.mbicycle.develop.weatherappmodule

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import by.mbicycle.develop.weatherappmodule.databinding.ActivitySplashScreenBinding
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsStates
import java.util.*
import kotlin.concurrent.timerTask

const val REQUEST_CODE_ACCESS_LOCATION = 2504

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val locationPermissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)
    private val locationController = LocationController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions(
                locationPermissions,

                {
                    locationController.checkLocationSettings {
                        if (it) initTransitionToMainScreen()
                    }
                },

                { deniedPermissions ->
                    requestPermissions(deniedPermissions, REQUEST_CODE_ACCESS_LOCATION)
                }
            )
        } else {
            locationController.checkLocationSettings {
                if (it) initTransitionToMainScreen()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_LOCATION_SETTINGS) {
            if (resultCode == RESULT_OK) {
                initTransitionToMainScreen()
            } else {
                closingApplication()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationController.checkLocationSettings {
                    if (it) initTransitionToMainScreen()
                }
            } else {
                showMessageNoData(permissions)
            }
        }
    }

    private fun initTransitionToMainScreen() {
        PreferencesManager.instance(this).apply {
            eraseData()
            locationController.requestLocationUpdate { location ->
                saveData(CoordinatesKeys.LONGITUDE, location.longitude.toFloat())
                saveData(CoordinatesKeys.LATITUDE, location.latitude.toFloat())
            }
        }

        binding.apply {
            generalViewsOnScreenGroup.visibility = View.VISIBLE
            messageNoLocation.root.visibility = View.GONE
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashScreenActivity, MainScreenActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }, 3000L)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showMessageNoData(permissions: Array<out String>) {
        binding.generalViewsOnScreenGroup.visibility = View.GONE
        permissions.forEach { permission ->
            if(shouldShowRequestPermissionRationale(permission)) {
                binding.messageNoLocation.apply {
                    root.visibility = View.VISIBLE
                    buttonAllowAccess.setOnClickListener {
                        requestPermissions(arrayOf(permission), REQUEST_CODE_ACCESS_LOCATION)
                    }
                }
            } else {
                AlertDialog.Builder(this).apply {
                    title = getString(R.string.permission_needed_dialog_title)
                    setMessage(getString(R.string.message_needed_location_permission))
                    setPositiveButton(getString(R.string.positive_button_name)) { _, _ ->
                        val intent = Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", packageName, null)
                        }

                        startActivity(intent)
                    }
                    setNegativeButton(getString(R.string.negative_button_name)) { _, _ ->
                        Logger.log("Cancelling Location Permission Alert Dialog")
                        closingApplication()
                    }
                }.create().show()
            }
        }
    }

    private fun closingApplication() {
        Toast.makeText(this, "Repeated denial of location request, application is closing",
            Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 5000L)
    }
}