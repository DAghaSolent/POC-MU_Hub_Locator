package com.example.testinggooglemaps

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testinggooglemaps.ui.theme.TestingGoogleMapsTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var isPermissionGranted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createLauncher()
        setContent {
            TestingGoogleMapsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MapComposable()
                }
            }
        }
    }

    fun createLauncher(){
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){isGranted ->
            if(isGranted){
                isPermissionGranted = true
                checkUserLocationPermissions()
            }else{
                Toast.makeText(this, "GPS Permissions Declined", Toast.LENGTH_SHORT).show()
                checkUserLocationPermissions()
            }
        }
    }

    fun checkUserLocationPermissions(){
        if(checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            isPermissionGranted = true
            fusedLocationClient.lastLocation.addOnSuccessListener {location : Location? ->
                if (location != null) {
                    Toast.makeText(this, "${location.latitude}, ${location.longitude}", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this, "GPS Location unavailable", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            //Request the Location Permissions From the User
            permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun MapComposable(){
        var postcodeInput by remember { mutableStateOf("") }

        val defaultCameraPosition = rememberCameraPositionState{
            position = CameraPosition.fromLatLngZoom(LatLng(50.92139183397814, -1.4320641306790338),
                12f)
        }

        val mapProperties by remember {
            mutableStateOf(MapProperties(mapType = MapType.TERRAIN))
        }

        Column(){
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                TextField(
                    value = postcodeInput,
                    onValueChange = {postcodeInput = it},
                    modifier = Modifier.weight(1f),
                    placeholder = {Text("Enter Postcode")}
                )

                Button(onClick = { postcodeInput = " "}, modifier = Modifier.padding(start = 8.dp)){
                    Icon(Icons.Filled.Clear, contentDescription = "Clear Postcode Text")
                }

                Button(onClick = { checkUserLocationPermissions() }, modifier = Modifier.padding(start = 8.dp)){
                    Image(painterResource(
                        id = R.drawable.access_location_icon),
                        contentDescription = "Access Location Icon"
                    )
                }
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = defaultCameraPosition,
                properties = mapProperties
            ){
                MarkerComposable(
                    state = MarkerState(position = LatLng(50.9161, -1.3649)),
                ){
                    Image(
                        painterResource(id = R.drawable.utilita),
                        contentDescription = null,
                        Modifier.size(36.dp)
                    )
                }

                MarkerComposable(
                    state = MarkerState(position = LatLng(50.92139183397814, -1.4320641306790338)),
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ){

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .background(Color.Red, shape = RoundedCornerShape(4.dp))
                        ){
                            Text(
                                text = "Utilita Energy Hub Shirley",
                                color = Color.White,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(2.dp)
                            )
                        }

                        Image(
                            painterResource(id = R.drawable.utilita),
                            contentDescription = null,
                            Modifier.size(36.dp)
                        )
                    }
                }

            }
        }
    }
}

