package by.mbicycle.develop.weatherappmodule

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import by.mbicycle.develop.weatherappmodule.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), BottomBarVisibilityListener {
    private lateinit var binding: ActivityMainBinding
    private val requestLocationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            requestLocationUpdates()
        } else {
            //пользоваль не дал доступ
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        val navigationAdapter = NavigationAdapter(this)
        binding.viewPager.apply {
            adapter = navigationAdapter
            isUserInputEnabled = false
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) {
            tab, position -> tab.text = when(position) {
                0 -> getString(R.string.city_tab_name)
                1 -> getString(R.string.daily_tab_name)
                2 -> getString(R.string.hourly_tab_name)
                else -> throw IllegalStateException()
            }
        }.attach()
    }

    override fun setBottomBarVisibility(visibility: Int) {
        binding.tabLayout.visibility = visibility
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager.fragments.first().childFragmentManager
        if (fragmentManager.fragments.size == 1) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Logger.log("lat=${location.latitude}, lon=${location.longitude}")
                locationManager.removeUpdates(this)
            }
        }

        locationManager.apply {
            if (isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0F,
                    locationListener)
            } else {
                //провайдер не доступен
            }
        }
    }
}