package com.example.testinggooglemaps

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testinggooglemaps.ui.theme.TestingGoogleMapsTheme
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
import com.example.testinggooglemaps.MapStyle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.MapStyleOptions
import java.io.IOException
import java.util.Locale
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.style.TextAlign
import com.google.maps.android.SphericalUtil
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private val mapViewModel : MapViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        invokePermissionsLauncher()
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

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun MapComposable(){
        var postcodeInput by remember { mutableStateOf("") }
        val mapStyle = MapStyle.styleJson
        val localContext = LocalContext.current
        val utilitaPOIs by mapViewModel.listUtilitaPOI.observeAsState(emptyList())

        // Setting default camera position to Shirley Utilita Energy Hub.
        val currentCameraPosition = rememberCameraPositionState{
            position = CameraPosition.fromLatLngZoom(LatLng(50.92094035265595, -1.4319340450913751),
                12f)
        }

        // Observing any changes within the Camera Viewmodel that could be potentially changed when
        // the application
        mapViewModel.cameraPositionLiveData.observe(this) { newCameraPosition ->
            currentCameraPosition.position = newCameraPosition
        }

        val mapProperties = MapProperties(
            mapStyleOptions = MapStyleOptions(mapStyle)
        )

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

                Button(onClick = {
                    val postcodeLatLng = getLatLngFromPostcode(localContext, postcodeInput)

                    if(postcodeLatLng != null){
                        currentCameraPosition.position = CameraPosition.fromLatLngZoom(postcodeLatLng, 12f)
                    }else{
                        Toast.makeText(localContext, "Postcode not found", Toast.LENGTH_LONG).show()
                    }
                },
                    modifier = Modifier.padding(start=8.dp)
                ){
                    Icon(Icons.Filled.Search, contentDescription = "Search with Postcode")
                }

                Button(onClick = { postcodeInput = " "}, modifier = Modifier.padding(start = 8.dp)){
                    Icon(Icons.Filled.Clear, contentDescription = "Clear Postcode Text")
                }

                Button(onClick = { getUserLocation() }, modifier = Modifier.padding(start = 8.dp)){
                    Image(painterResource(
                        id = R.drawable.access_location_icon),
                        contentDescription = "Access Location Icon"
                    )
                }
            }

            Column(modifier = Modifier.fillMaxSize()) {

                Box(modifier = Modifier.weight(1.25f)){
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = currentCameraPosition,
                        properties = mapProperties
                    ){
                        utilitaPOIs.forEach { utilitaPOI ->
                            MarkerComposable(
                                state = MarkerState(position = LatLng(utilitaPOI.lat, utilitaPOI.lon))
                            ){
                                Image(
                                    painterResource(id = R.drawable.utilita),
                                    contentDescription = null,
                                    Modifier.size(36.dp)
                                )
                            }
                        }

                    }
                }

                Box(modifier = Modifier.weight(0.75f)){
                    LazyColumn {
                        items(utilitaPOIs) { utilitaPOI ->
                            val distanceUserLocToHub = calculateDistance(utilitaPOI, currentCameraPosition.position)
                            val twoDecimalFormattingDistance = DecimalFormat("#.##").apply {
                                roundingMode = RoundingMode.DOWN
                            }.format(distanceUserLocToHub)

                            Text("${utilitaPOI.description}\n" +
                                    "${utilitaPOI.address}, ${utilitaPOI.city_town}, ${utilitaPOI.postcode}\n" +
                                    "${utilitaPOI.phone_number}\n${utilitaPOI.emailAddress}\n" +
                                    "${twoDecimalFormattingDistance} Miles",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(width = 1.dp, color = Color.Magenta)
                                    .padding(8.dp)
                                )
                        }
                    }
                }

            }
        }
    }
    fun getLatLngFromPostcode(context: Context, postcode: String): LatLng? {
        val geocoder = Geocoder(context, Locale.getDefault())

        try{
            val address: MutableList<Address>? = geocoder.getFromLocationName(postcode, 1)

            if(!address.isNullOrEmpty()){
                return LatLng(address[0].latitude, address[0].longitude)
            }
        }catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun invokePermissionsLauncher(){
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
            if(isGranted){
                getUserLocation()
            }else{
                Toast.makeText(this, "GPS Permissions Declined", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun getUserLocation(){
        if(checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationClient.lastLocation.addOnSuccessListener {location : Location? ->
                if (location != null) {
                    val userLocation = LatLng(location.latitude, location.longitude)
                    mapViewModel.cameraPositionLiveData.value = CameraPosition.fromLatLngZoom(userLocation,12f)
                }else{
                    Toast.makeText(this, "GPS Location unavailable", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            //Request the Location Permissions From the User
            permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    fun calculateDistance(poi: UtilitaPOI, cameraPosition : CameraPosition): Double {
        val distanceMetres = SphericalUtil.computeDistanceBetween(cameraPosition.target,(LatLng(poi.lat, poi.lon)))
        // Convert overall distance calculation from Meters to Miles for UK Format.
        val distanceMiles =  distanceMetres / 1606.34

        return distanceMiles
    }
}