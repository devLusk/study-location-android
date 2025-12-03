package com.github.devlusk.locationstudyapp

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.github.devlusk.locationstudyapp.ui.theme.LocationStudyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LocationStudyAppTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)

    LocationScreen(locationUtils, context)
}

@Composable
fun LocationScreen(
    locationUtils: LocationUtils,
    context: Context
) {
    val permissionRequestLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val isCoarsePermissionGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            val isFinePermissionGranted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

            if (isCoarsePermissionGranted || isFinePermissionGranted) {
                    // Access granted to location
            } else {
                val shouldShowRationale =
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        context as MainActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )

                if (shouldShowRationale) {
                    Toast.makeText(
                        context,
                        "Location permission is required to use this feature",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Go to Settings to change the location permission",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text("Location not available")

            Button(
                onClick = {
                    if (locationUtils.hasLocationPermission()) {
                        // Permission granted, update the location
                    } else {
                        permissionRequestLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        )
                    }
                }
            ) {
                Text("Get Location")
            }
        }
    }
}