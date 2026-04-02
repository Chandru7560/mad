package com.lab.allinonelab

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.Locale

// ============================================================
// TOPIC 6: LOCATION BASED SERVICES
// ============================================================
// Uses LocationManager to get GPS coordinates.
// Uses Geocoder to convert coordinates to address (Reverse Geocoding).
// Demonstrates runtime permission handling.

class LocationActivity : AppCompatActivity(), LocationListener {

    private lateinit var locationManager: LocationManager
    private lateinit var tvLatitude: TextView
    private lateinit var tvLongitude: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvStatus: TextView

    private val LOCATION_PERMISSION_CODE = 100
    private var currentLatitude = 0.0
    private var currentLongitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        title = "Location Services"

        tvLatitude = findViewById(R.id.tvLatitude)
        tvLongitude = findViewById(R.id.tvLongitude)
        tvAddress = findViewById(R.id.tvAddress)
        tvStatus = findViewById(R.id.tvLocationStatus)

        // Get LocationManager system service
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        // --- Get Location Button ---
        findViewById<Button>(R.id.btnGetLocation).setOnClickListener {
            getLocation()
        }

        // --- Open Map Button ---
        findViewById<Button>(R.id.btnOpenMap).setOnClickListener {
            openMap()
        }
    }

    private fun getLocation() {
        // Step 1: Check if permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission at runtime
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_CODE
            )
        } else {
            // Permission already granted - request location updates
            tvStatus.text = "Status: Fetching location..."

            // Request location updates from GPS provider
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,   // minimum time interval (5 seconds)
                5f,     // minimum distance (5 meters)
                this    // LocationListener (this activity)
            )

            // Also try network provider as fallback
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                5000, 5f, this
            )

            // Try to get last known location immediately
            val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            if (lastLocation != null) {
                updateLocationUI(lastLocation)
            }
        }
    }

    // Called when location changes
    override fun onLocationChanged(location: Location) {
        updateLocationUI(location)
    }

    private fun updateLocationUI(location: Location) {
        currentLatitude = location.latitude
        currentLongitude = location.longitude

        tvLatitude.text = "Latitude: $currentLatitude"
        tvLongitude.text = "Longitude: $currentLongitude"
        tvStatus.text = "Status: Location found!"

        // --- Reverse Geocoding: Convert coordinates to address ---
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val fullAddress = address.getAddressLine(0)
                tvAddress.text = "Address: $fullAddress"
            }
        } catch (e: Exception) {
            tvAddress.text = "Address: Unable to get address"
        }
    }

    // Open location in Google Maps using implicit intent
    private fun openMap() {
        if (currentLatitude != 0.0 && currentLongitude != 0.0) {
            val uri = Uri.parse("geo:$currentLatitude,$currentLongitude?q=$currentLatitude,$currentLongitude")
            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        } else {
            Toast.makeText(this, "Get location first!", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                Toast.makeText(this, "Location permission denied!", Toast.LENGTH_SHORT).show()
                tvStatus.text = "Status: Permission denied"
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Stop location updates when activity is paused
        locationManager.removeUpdates(this)
    }
}
